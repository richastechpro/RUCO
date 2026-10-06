package com.ruco.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ruco.model.City;
import com.ruco.model.FBO;
import com.ruco.model.FBOBranch;
import com.ruco.model.FBOBranchLogin;
import com.ruco.model.State;
import com.ruco.model.User;
import com.ruco.projection.StateCityProjection;
import com.ruco.repo.CityRepository;
import com.ruco.repo.FboBranchLoginRepository;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.FboRepository;
import com.ruco.repo.StateRepository;
import com.ruco.request.FBOBranchLoginRequest;
import com.ruco.response.CityResponse;
import com.ruco.response.FboBranchLoginResponse;
import com.ruco.response.FboBranchResponse;
import com.ruco.response.StateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FboBranchLoginService {

	private final FboBranchLoginRepository fboBranchLoginRepository;
	private final FboRepository fboRepository;
	private final FboBranchRepository fboBranchRepository;
	private final PasswordEncoder encoder;
	private final StateRepository stateRepository;
	private final CityRepository cityRepository;

	public void create(FBOBranchLoginRequest request, Long fboId) {
		Optional<FBO> fboOptional = fboRepository.findById(fboId);
		if (fboOptional.isPresent()) {
			FBO fbo = fboOptional.get();
			FBOBranchLogin l = new FBOBranchLogin();
			l.setPwd(request.getPassword());
			l.setPassword(encoder.encode(request.getPassword()));
			l.setUsername(request.getUsername());
			l.setEmail(request.getEmail());
			l.setMobileNo(request.getMobileNo());
			l.setType(request.getType());
			l.setStateId(request.getStateId());
			l.setCityId(request.getCityId());
			l.setFboBranchId(request.getFboBranchId());
			l.setFbo(fbo);
			fboBranchLoginRepository.save(l);
		}
	}

	public FBOBranchLogin get(Integer id) {

		return null;
	}

	public void update(FBOBranchLogin data) {

	}

	public void delete(Long id) {
		Optional<FBOBranchLogin> fboLoginOptional = fboBranchLoginRepository.findById(id);
		if (fboLoginOptional.isPresent()) {
			FBOBranchLogin fboBranchLogin = fboLoginOptional.get();
			fboBranchLogin.setIsDelete(true);
			fboBranchLoginRepository.save(fboBranchLogin);
		}
	}

	public FboBranchLoginResponse getById(Long fboLoginId) {
		Optional<FBOBranchLogin> fboLoginOptional = fboBranchLoginRepository.findById(fboLoginId);
		if (fboLoginOptional.isPresent()) {
			FBOBranchLogin l = fboLoginOptional.get();
			FboBranchLoginResponse response = new FboBranchLoginResponse();
			response.setId(l.getId());
			response.setUsername(l.getUsername());
			response.setEmail(l.getEmail());
			response.setMobileNo(l.getMobileNo());
			response.setType(l.getType());
			response.setPassword(l.getPwd());
			Long stateId = l.getStateId();
			if (stateId != null) {
				Optional<State> stateOptional = stateRepository.findById(stateId);
				if (stateOptional.isPresent()) {
					State state = stateOptional.get();
					StateResponse stateResponse = new StateResponse();
					stateResponse.setStateId(state.getId());
					stateResponse.setStateName(state.getStateName());
					response.setState(stateResponse);
				}
			}
			Long cityId = l.getCityId();
			if (cityId != null) {
				Optional<City> cityOptional = cityRepository.findById(cityId);
				if (cityOptional.isPresent()) {
					City city = cityOptional.get();
					CityResponse cityResponse = new CityResponse();
					cityResponse.setCityId(city.getId());
					cityResponse.setCityName(city.getCityName());
					response.setCity(cityResponse);
				}
			}
			Long fboBranchId = l.getFboBranchId();
			if (fboBranchId != null) {
				Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
				if (fboBranchOptional.isPresent()) {
					FBOBranch fboBranch = fboBranchOptional.get();
					FboBranchResponse fboBranchResponse = new FboBranchResponse();
					fboBranchResponse.setId(fboBranch.getId());
					fboBranchResponse.setFboBranchName(fboBranch.getFboBranchName());
					response.setFboBranch(fboBranchResponse);
				}
			}
			return response;
		}
		return null;
	}

	public List<User> getPickupBoys(Long collectionId) {
		// Optional<Collection> collectionOptional =
		// collectionRepository.findById(collectionId);
		// if (collectionOptional.isPresent()) {
		// Collection collection = collectionOptional.get();
		// if (collection.getType().equals(CollectionType.SINGLE)) {
		// Request request = collection.getRequests().get(0);
		// FBOBranch fboBranch = request.getBranch();
		// Integer fboId = fboBranchRepository.getFboId(fboBranch.getId());
		// Optional<FBO> fboOptional = fboRepository.findById(fboId);
		// if (fboOptional.isPresent()) {
		// FBO fbo = fboOptional.get();
		// List<FBOBranchLogin> pickupBoys =
		// fboLoginRepository.getPickupBoys(fbo.getId());
		// return pickupBoys.stream().filter(l ->
		// !l.getIsDelete()).collect(Collectors.toList());
		// }
		// }

		// else if (collection.getType().equals(CollectionType.BULK)) {
		// List<Integer> fboBranchIds = collection.getRequests().stream()
		// .map(r -> r.getBranch().getId())
		// .collect(Collectors.toList());
		// List<Integer> fboIds = fboBranchRepository.getFboIds(fboBranchIds);
		// List<FBOBranchLogin> pickupBoys = new ArrayList<>();
		// for (Integer fboId : fboIds) {
		// List<FBOBranchLogin> fboBranchLogins =
		// fboLoginRepository.getPickupBoys(fboId);
		// pickupBoys.addAll(
		// fboBranchLogins.stream().filter(l ->
		// !l.getIsDelete()).collect(Collectors.toList()));
		// }
		// return pickupBoys;
		// }
		// }
		// return new ArrayList<>();

//		List<User> users= userRepository.getPickupBoys();
		return null;
	}

	public ResponseEntity<?> getSampleFormat(Long fboId) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("FBO Branch Login Upload Format");
			String[] headers = { "email", "password", "username", "mobile_no", "type", "state_id", "city_id",
					"fbo_branch_id" };

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

			sheet = workbook.createSheet("FBO Branch");
			headers = new String[] { "fbo_branch_id", "fbo_branch_name" };

			row = 0;
			headerRow = sheet.createRow(row++);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(getHeaderCellStyle(workbook));
			}

			Optional<FBO> fboOptional = fboRepository.findById(fboId);
			if (fboOptional.isPresent()) {
				FBO fbo = fboOptional.get();
				List<FBOBranch> fboBranches = fbo.getFboBranches();
				for (int i = 0; i < fboBranches.size(); i++) {
					int col = 0;
					Row datarow = sheet.createRow(row++);
					FBOBranch branch = fboBranches.get(i);
					Cell cell = datarow.createCell(col++);
					cell.setCellValue(branch.getId());
					cell.setCellStyle(getColStyle(workbook));
					cell = datarow.createCell(col++);
					cell.setCellValue(branch.getFboBranchName());
					cell.setCellStyle(getColStyle(workbook));
				}

				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}
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
			List<FBOBranchLogin> rowData = new ArrayList<>();

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
					FBOBranchLogin fboBranchLogin = new FBOBranchLogin();

					String email = row.getCell(col++).getStringCellValue();
					String password = row.getCell(col++).getStringCellValue();
					String username = row.getCell(col++).getStringCellValue();
					Long mobileNo = (long) row.getCell(col++).getNumericCellValue();
					String type = row.getCell(col++).getStringCellValue();
					if (type.equals("FBO_STATE") || type.equals("FBO_CITY")) {
						Long stateId = (long) row.getCell(col++).getNumericCellValue();
						fboBranchLogin.setStateId(stateId);
					}
					if (type.equals("FBO_CITY")) {
						Long cityId = (long) row.getCell(col++).getNumericCellValue();
						fboBranchLogin.setCityId(cityId);
					}
					if (type.equals("FBO_BRANCH")) {
						Long fboBranchId = (long) row.getCell(col++).getNumericCellValue();
						fboBranchLogin.setFboBranchId(fboBranchId);
					}

					fboBranchLogin.setEmail(email);
					fboBranchLogin.setPassword(encoder.encode(password));
					fboBranchLogin.setPwd(password);
					fboBranchLogin.setUsername(username);
					fboBranchLogin.setMobileNo(mobileNo);
					fboBranchLogin.setType(type);
					fboBranchLogin.setFbo(fbo);

					rowData.add(fboBranchLogin);
				}

				rowData.forEach(fboBranchLoginRepository::save);

			}

			return ResponseEntity.ok().body(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
