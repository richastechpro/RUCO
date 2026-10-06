package com.ruco.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ruco.config.CustomUserDetails;
import com.ruco.constant.FBOUserType;
import com.ruco.constant.UserType;
import com.ruco.model.Branch;
import com.ruco.model.FBO;
import com.ruco.model.FBOBranch;
import com.ruco.model.Rate;
import com.ruco.model.RateRequest;
import com.ruco.projection.StateCityProjection;
import com.ruco.repo.AreaRepository;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CityRepository;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.FboRepository;
import com.ruco.repo.RateRepository;
import com.ruco.repo.RateRequestRepository;
import com.ruco.repo.StateRepository;
import com.ruco.request.FBOBranchRateRequest;
import com.ruco.request.FBOBranchRequest;
import com.ruco.response.AreaResponse;
import com.ruco.response.BranchResponse;
import com.ruco.response.CityResponse;
import com.ruco.response.FboBranchResponse;
import com.ruco.response.StateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FboBranchService {

	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final AreaRepository areaRepository;
	private final FboRepository fboRepository;
	private final FboBranchRepository fboBranchRepository;
	private final BranchRepository branchRepository;
	private final UserService userService;
	private final RateRepository rateRepository;
	private final RateRequestRepository rateRequestRepository;

	public void create(FBOBranchRequest request, Long fboId) {
		Optional<FBO> fboOptional = fboRepository.findById(fboId);
		if (fboOptional.isPresent()) {
			FBO fbo = fboOptional.get();
			FBOBranch fboBranch = new FBOBranch();
			fboBranch.setFboBranchName(request.getFboBranchName());
			fboBranch.setFboBranchAddress(request.getFboBranchAddress());
			fboBranch.setRepresentative(request.getRepresentative());
			fboBranch.setStateId(request.getStateId());
			fboBranch.setCityId(request.getCityId());
			fboBranch.setAreaId(request.getAreaId());
			fboBranch.setBranchId(request.getBranchId());
			fboBranch.setEmail(request.getEmail());
			fboBranch.setMobileNo(request.getMobileNo());
			fboBranch.setGstNo(request.getGstNo());
			fboBranch.setFssaiNo(request.getFssaiNo());

			fboBranch.setFbo(fbo);
			fboBranch = fboBranchRepository.save(fboBranch);

			Float price = request.getRate();
			rateRepository.save(new Rate(price, fboBranch.getId()));

		}
	}

	public FboBranchResponse getFboBranch(Long id) {
		Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(id);
		if (fboBranchOptional.isPresent()) {
			FBOBranch fboBranch = fboBranchOptional.get();
			FboBranchResponse response = new FboBranchResponse();
			response.setId(fboBranch.getId());
			response.setFboBranchName(fboBranch.getFboBranchName());
			response.setFboBranchAddress(fboBranch.getFboBranchAddress());
			response.setRepresentative(fboBranch.getRepresentative());
			response.setEmail(fboBranch.getEmail());
			response.setMobileNo(fboBranch.getMobileNo());
			response.setGstNo(fboBranch.getGstNo());
			response.setFssaiNo(fboBranch.getFssaiNo());
			response.setState(
					fboBranch.getStateId() != null ? stateRepository.findById(fboBranch.getStateId()).map(s -> {
						StateResponse stateResponse = new StateResponse();
						stateResponse.setStateId(s.getId());
						stateResponse.setStateName(s.getStateName());
						return stateResponse;
					}).orElse(null) : null);

			response.setCity(fboBranch.getCityId() != null ? cityRepository.findById(fboBranch.getCityId()).map(s -> {
				CityResponse cityResponse = new CityResponse();
				cityResponse.setCityId(s.getId());
				cityResponse.setCityName(s.getCityName());
				return cityResponse;
			}).orElse(null) : null);

			response.setArea(fboBranch.getAreaId() != null ? areaRepository.findById(fboBranch.getAreaId()).map(s -> {
				AreaResponse areaResponse = new AreaResponse();
				areaResponse.setAreaId(s.getId());
				areaResponse.setAreaName(s.getAreaName());
				return areaResponse;
			}).orElse(null) : null);

			response.setBranch(
					fboBranch.getBranchId() != null ? branchRepository.findById(fboBranch.getBranchId()).map(b -> {
						BranchResponse branchResponse = new BranchResponse();
						branchResponse.setId(b.getId());
						branchResponse.setBranchName(b.getBranchName());
						return branchResponse;
					}).orElse(null) : null);
			response.setRate(rateRepository.getRate(id).orElse(new Rate(0f)));
			return response;
		}
		return null;
	}

	public void update(FBOBranchRequest request, Long fboId) {
		Optional<FBO> fboOptional = fboRepository.findById(fboId);
		if (fboOptional.isPresent()) {
			FBO fbo = fboOptional.get();
			Long id = request.getId();
			Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(id);
			if (fboBranchOptional.isPresent()) {
				FBOBranch fboBranch = fboBranchOptional.get();
				fboBranch.setFboBranchName(request.getFboBranchName());
				fboBranch.setFboBranchAddress(request.getFboBranchAddress());
				fboBranch.setRepresentative(request.getRepresentative());
				fboBranch.setStateId(request.getStateId());
				fboBranch.setCityId(request.getCityId());
				fboBranch.setAreaId(request.getAreaId());
				fboBranch.setEmail(request.getEmail());
				fboBranch.setMobileNo(request.getMobileNo());
				fboBranch.setGstNo(request.getGstNo());
				fboBranch.setFssaiNo(request.getFssaiNo());
				fboBranch.setBranchId(request.getBranchId());
				fboBranch.setFbo(fbo);
				Optional<Rate> rateOptional = rateRepository.getRate(id);
				if (rateOptional.isPresent()) {
					Rate rate = rateOptional.get();
					rate.setPrice(request.getRate());
					rateRepository.save(rate);
				}
				fboBranchRepository.save(fboBranch);
			}

		}
	}

	public void delete(Long id) {
		Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(id);
		if (fboBranchOptional.isPresent()) {
			FBOBranch fboBranch = fboBranchOptional.get();
			fboBranch.setIsDelete(true);
			Optional<Rate> rateOptional = rateRepository.getRate(id);
			if (rateOptional.isPresent()) {
				Rate rate = rateOptional.get();
				rate.setIsDelete(true);
				rateRepository.save(rate);
			}
			fboBranchRepository.save(fboBranch);
		}
	}

	public List<FboBranchResponse> getValidFboBranches(Long fboId) {
		Optional<FBO> fboOptional = fboRepository.findById(fboId);
		if (fboOptional.isPresent()) {
			FBO fbo = fboOptional.get();
			List<FBOBranch> fboBranches = fbo.getFboBranches();
			return fboBranches.stream().map(fboBranch -> {
				FboBranchResponse response = new FboBranchResponse();
				response.setId(fboBranch.getId());
				response.setFboBranchName(fboBranch.getFboBranchName());
				return response;
			}).collect(Collectors.toList());
		}
		return null;
	}

	public List<FboBranchResponse> getAllValid() {

		CustomUserDetails user = userService.getUser();
		Boolean isFboUser = user.getIsFboUser();
		String userType = user.getUserType();
		if (isFboUser) {
			if (userType.equals(FBOUserType.FBO_SUPERADMIN)) {
				Long fboId = user.getFboId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					return fbo.getFboBranches().stream().map(this::mapData).collect(Collectors.toList());
				}
			}

			else if (userType.equals(FBOUserType.FBO_STATE)) {
				Long fboId = user.getFboId();
				Long stateId = user.getStateId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					return fbo.getFboBranches().stream().filter(
							fboBranch -> fboBranch.getStateId() != null && fboBranch.getStateId().equals(stateId))
							.map(this::mapData).collect(Collectors.toList());
				}
			}

			else if (userType.equals(FBOUserType.FBO_CITY)) {
				Long fboId = user.getFboId();
				Long stateId = user.getStateId();
				Long cityId = user.getCityId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					return fbo.getFboBranches().stream()
							.filter(fboBranch -> fboBranch.getStateId() != null
									&& fboBranch.getStateId().equals(stateId) && fboBranch.getCityId() != null
									&& fboBranch.getCityId().equals(cityId))
							.map(this::mapData).collect(Collectors.toList());
				}
			}

			else if (userType.equals(FBOUserType.FBO_BRANCH)) {
				Long fboBranchId = user.getFboBranchId();
				return fboBranchRepository.findById(fboBranchId)
						.map(fboBranch -> Arrays.asList(this.mapData(fboBranch))).orElse(null);
			}
		}

		else {
			if (userType.equals(UserType.MANAGER)) {
				Long branchId = user.getBranchId();
				List<FBOBranch> fboBranchs = fboBranchRepository.getFboBranchsBranchId(branchId);
				return fboBranchs.stream().map(this::mapData).collect(Collectors.toList());
			}

			else if (userType.equals(UserType.SUPERADMIN)) {
				List<FBOBranch> fboBranches = fboBranchRepository.findAllByIsDeleteFalse();
				return fboBranches.stream().map(this::mapData).collect(Collectors.toList());
			}
		}

		return null;
	}

	private FboBranchResponse mapData(FBOBranch fboBranch) {
		FboBranchResponse response = new FboBranchResponse();
		response.setId(fboBranch.getId());
		response.setFboBranchName(fboBranch.getFboBranchName());
		response.setFboBranchAddress(fboBranch.getFboBranchAddress());
		response.setRepresentative(fboBranch.getRepresentative());
		response.setEmail(fboBranch.getEmail());
		response.setMobileNo(fboBranch.getMobileNo());
		response.setGstNo(fboBranch.getGstNo());
		response.setFssaiNo(fboBranch.getFssaiNo());
		response.setState(fboBranch.getStateId() != null ? stateRepository.findById(fboBranch.getStateId()).map(s -> {
			StateResponse stateResponse = new StateResponse();
			stateResponse.setStateId(s.getId());
			stateResponse.setStateName(s.getStateName());
			return stateResponse;
		}).orElse(null) : null);

		response.setCity(fboBranch.getCityId() != null ? cityRepository.findById(fboBranch.getCityId()).map(s -> {
			CityResponse cityResponse = new CityResponse();
			cityResponse.setCityId(s.getId());
			cityResponse.setCityName(s.getCityName());
			return cityResponse;
		}).orElse(null) : null);

		response.setArea(fboBranch.getAreaId() != null ? areaRepository.findById(fboBranch.getAreaId()).map(s -> {
			AreaResponse areaResponse = new AreaResponse();
			areaResponse.setAreaId(s.getId());
			areaResponse.setAreaName(s.getAreaName());
			return areaResponse;
		}).orElse(null) : null);
		return response;
	}

	public List<FboBranchResponse> loadFboBranchByBranchId(Long branchId) {
		List<FBOBranch> branchs = fboBranchRepository.getFboBranchesByBranchId(branchId);
		return branchs.stream().map(this::mapData).collect(Collectors.toList());
	}

	public ResponseEntity<?> getSampleFormat() {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("FBO Branch Upload Format");
			String[] headers = { "fb_branch_name", "address", "representative", "state", "city", "branch", "email",
					"mobile_no", "gst_no", "fssai_no" };

			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(getHeaderCellStyle(workbook));
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			sheet = workbook.createSheet("State");
			headers = new String[] { "state_id", "state_name", "city_id", "city_name" };

			int row = 0;
			headerRow = sheet.createRow(row++);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(getHeaderCellStyle(workbook));
			}

			List<StateCityProjection> states = stateRepository.getAllValidStateCity();
			for (int i = 0; i < states.size(); i++) {
				int col = 0;
				Row datarow = sheet.createRow(row++);
				StateCityProjection state = states.get(i);
				Cell cell = datarow.createCell(col++);
				cell.setCellValue(state.getStateid());
				cell.setCellStyle(getColStyle(workbook));
				cell = datarow.createCell(col++);
				cell.setCellValue(state.getStatename());
				cell.setCellStyle(getColStyle(workbook));
				cell = datarow.createCell(col++);
				cell.setCellValue(state.getCityid());
				cell.setCellStyle(getColStyle(workbook));
				cell = datarow.createCell(col++);
				cell.setCellValue(state.getCityname());
				cell.setCellStyle(getColStyle(workbook));
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			sheet = workbook.createSheet("Branch");
			headers = new String[] { "branch_id", "branch_name" };

			row = 0;
			headerRow = sheet.createRow(row++);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(getHeaderCellStyle(workbook));
			}

			List<Branch> branches = branchRepository.findAllByIsDeleteFalse();
			for (int i = 0; i < branches.size(); i++) {
				int col = 0;
				Row datarow = sheet.createRow(row++);
				Branch branch = branches.get(i);
				Cell cell = datarow.createCell(col++);
				cell.setCellValue(branch.getId());
				cell.setCellStyle(getColStyle(workbook));
				cell = datarow.createCell(col++);
				cell.setCellValue(branch.getBranchName());
				cell.setCellStyle(getColStyle(workbook));
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);

			HttpHeaders headersResponse = new HttpHeaders();
			headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sample-format.xlsx");
			return ResponseEntity.ok().headers(headersResponse).body(out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating Excel file");
		}
	}

	private CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	private CellStyle getColStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	public ResponseEntity<?> upload(MultipartFile file, Long fboId) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			List<FBOBranch> rowData = new ArrayList<>();

			Optional<FBO> fbOptional = fboRepository.findById(fboId);
			if (fbOptional.isPresent()) {
				FBO fbo = fbOptional.get();

				int rowNum = 0;
				for (Row row : sheet) {

					if (rowNum++ == 0) {
						continue;
					}

					if (rowNum > 256) {
						System.out.println(rowNum);
					}

					int col = 0;
					FBOBranch fboBranch = new FBOBranch();

					String fboBranchName = row.getCell(col++).getStringCellValue();
					String address = row.getCell(col++).getStringCellValue();
					String representative = row.getCell(col++).getStringCellValue();
					Long stateId = (long) row.getCell(col++).getNumericCellValue();
					Long cityId = (long) row.getCell(col++).getNumericCellValue();
					Long branchId = (long) row.getCell(col++).getNumericCellValue();
					String email = row.getCell(col++).getStringCellValue();
					Long mobileNo = (long) row.getCell(col++).getNumericCellValue();
					String gstNo = row.getCell(col++).getStringCellValue();
					String fssaiNo = row.getCell(col++).getStringCellValue();

					fboBranch.setFboBranchName(fboBranchName);
					fboBranch.setFboBranchAddress(address);
					fboBranch.setRepresentative(representative);
					fboBranch.setStateId(stateId);
					fboBranch.setCityId(cityId);
					fboBranch.setBranchId(branchId);
					fboBranch.setEmail(email);
					fboBranch.setMobileNo(mobileNo);
					fboBranch.setGstNo(gstNo);
					fboBranch.setFssaiNo(fssaiNo);
					fboBranch.setFbo(fbo);

					rowData.add(fboBranch);
				}

				rowData.forEach(fboBranchRepository::save);

			}

			return ResponseEntity.ok().body(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public void createRateRequest(FBOBranchRateRequest request) {
		CustomUserDetails user = userService.getUser();
		Long userId = user.getId();
		String username = user.getUsername();
		RateRequest rateRequest = new RateRequest();
		Long fboBranchId = request.getFboBranchId();
		rateRequest.setNewPrice(request.getRate());
		rateRequest.setFboBranchId(fboBranchId);
		rateRequest.setFboBranchName(request.getFboBranchName());
		rateRequest.setUserId(userId);
		rateRequest.setUsername(username);
		Optional<Rate> rateOptional = rateRepository.getRate(fboBranchId);
		if (rateOptional.isPresent()) {
			Rate rate = rateOptional.get();
			Float oldPrice = rate.getPrice();
			rateRequest.setOldPrice(oldPrice);
		}
		rateRequestRepository.save(rateRequest);
	}

}
