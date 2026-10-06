package com.ruco.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.postmarkapp.postmark.client.exception.PostmarkException;
import com.ruco.config.CustomUserDetails;
import com.ruco.constant.CollectionType;
import com.ruco.constant.FBOUserType;
import com.ruco.constant.SearchBuilder;
import com.ruco.constant.UserType;
import com.ruco.model.Branch;
import com.ruco.model.Collection;
import com.ruco.model.CollectionItem;
import com.ruco.model.Customer;
import com.ruco.model.FBO;
import com.ruco.model.FBOBranch;
import com.ruco.model.Image;
import com.ruco.model.InventoryIn;
import com.ruco.model.InventoryOut;
import com.ruco.model.Rate;
import com.ruco.model.Warehouse;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CollectionItemRepository;
import com.ruco.repo.CollectionRepository;
import com.ruco.repo.CustomerRepository;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.FboRepository;
import com.ruco.repo.ImageRepository;
import com.ruco.repo.InventoryInRepository;
import com.ruco.repo.InventoryOutRepository;
import com.ruco.repo.RateRepository;
import com.ruco.repo.WarehouseRepository;
import com.ruco.request.BulkCollecionRequest;
import com.ruco.request.CollectionAssignRequest;
import com.ruco.request.CollectionCollectRequest;
import com.ruco.request.CollectionItemRequest;
import com.ruco.request.InventoryOutRequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.BulkCollectionResponse;
import com.ruco.response.CertficateResponse;
import com.ruco.response.CollectionItemDetail;
import com.ruco.response.CollectionItemResponse;
import com.ruco.response.CollectionResponse;
import com.ruco.response.InventoryInResponse;
import com.ruco.response.InventoryOutResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollectionService {

	private final CollectionRepository collectionRepository;
	private final CollectionItemRepository collectionItemRepository;
	private final FboBranchRepository fboBranchRepository;
	private final UserService userService;
	private final ImageRepository imageRepository;
	private final InventoryInRepository inventoryInRepository;
	private final InventoryOutRepository inventoryOutRepository;
	private final BranchRepository branchRepository;
	private final CustomerRepository customerRepository;
	private final WarehouseRepository warehouseRepository;
	private final FboRepository fboRepository;
	private final RateRepository rateRepository;
	private final MailService mailService;
	private Map<Integer, LocalDateTime> otpMap = new HashMap<>();

	@Value("${upload.file.locn}")
	private String filePath;

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		CustomUserDetails user = userService.getUser();
		Boolean isFboUser = user.getIsFboUser();
		String userType = user.getUserType();
		Long userId = user.getId();
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "id"));

		if (isFboUser) {
			Long fboId = user.getFboId();
			if (userType.equals(FBOUserType.FBO_SUPERADMIN)) {
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<Long> fboBranchIds = fbo.getFboBranches().stream().map(FBOBranch::getId)
							.collect(Collectors.toList());
					Page<Collection> page = collectionRepository.findAll(
							SearchBuilder.collectionHasSearchText(searchTxt, fboBranchIds, null, null), pageable);
					return page.map(this::mapData);
				}
			}

			else if (userType.equals(FBOUserType.FBO_STATE)) {
				Long userStateId = user.getStateId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<Long> fboBranchIds = fbo.getFboBranches().stream().filter(
							fboBranch -> fboBranch.getStateId() != null && fboBranch.getStateId().equals(userStateId))
							.map(FBOBranch::getId).collect(Collectors.toList());
					Page<Collection> page = collectionRepository.findAll(
							SearchBuilder.collectionHasSearchText(searchTxt, fboBranchIds, null, null), pageable);
					return page.map(this::mapData);
				}
			}

			else if (userType.equals(FBOUserType.FBO_CITY)) {
				Long userStateId = user.getStateId();
				Long userCityId = user.getCityId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<Long> fboBranchIds = fbo.getFboBranches().stream()
							.filter(fboBranch -> fboBranch.getStateId() != null
									&& fboBranch.getStateId().equals(userStateId) && fboBranch.getCityId() != null
									&& fboBranch.getCityId().equals(userCityId))
							.map(FBOBranch::getId).collect(Collectors.toList());
					Page<Collection> page = collectionRepository.findAll(
							SearchBuilder.collectionHasSearchText(searchTxt, fboBranchIds, null, null), pageable);
					return page.map(this::mapData);
				}
			}

			else if (userType.equals(FBOUserType.FBO_BRANCH)) {
				Long fboBranchId = user.getFboBranchId();
				Page<Collection> page = collectionRepository.findAll(
						SearchBuilder.collectionHasSearchText(searchTxt, Arrays.asList(fboBranchId), null, null),
						pageable);
				return page.map(this::mapData);
			}

		}

		else {

			if (userType.equals(UserType.MANAGER)) {
				Long branchId = user.getBranchId();
				Page<Collection> page = collectionRepository
						.findAll(SearchBuilder.collectionHasSearchText(searchTxt, null, branchId, null), pageable);
				return page.map(this::mapData);
			}

			else if (userType.equals(UserType.PICKUPBOY)) {
				Long branchId = user.getBranchId();
				Page<Collection> page = collectionRepository
						.findAll(SearchBuilder.collectionHasSearchText(searchTxt, null, branchId, userId), pageable);
				return page.map(this::mapData);
			}

			else if (userType.equals(UserType.SUPERADMIN)) {
				Page<Collection> page = collectionRepository
						.findAll(SearchBuilder.collectionHasSearchText(searchTxt, null, null, null), pageable);
				return page.map(this::mapData);
			}
		}

		return null;
	}

	private CollectionResponse mapData(Collection collection) {
		String type = collection.getCollectionType();

		if (type.equals(CollectionType.SINGLE)) {
			List<CollectionItem> requests = collection.getItems();
			CollectionItem collectionItem = requests.get(0);
			CollectionResponse response = new CollectionResponse();
			response.setId(collection.getId());
			response.setCollectionName(collection.getCollectionName());
			response.setRequestDate(collectionItem.getRequestDate());
			response.setQty(collectionItem.getQty());
			response.setIsAssigned(collection.getIsAssigned());
			Integer notCollected = 0;
			Integer fullyCollected = 2;
			response.setCollected(collectionItem.getIsCollected() ? fullyCollected : notCollected);
			response.setType(collection.getCollectionType());
			response.setRequestId(collectionItem.getId());
			response.setInventoryIn(collectionItem.getInventoryIn() != null);
			response.setCollectedQty(collectionItem.getCollectedQty());
			Long fboBranchId = collectionItem.getFboBranchId();
			Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
			if (fboBranchOptional.isPresent()) {
				FBOBranch fboBranch = fboBranchOptional.get();
				String fboBranchName = fboBranch.getFboBranchName();
				response.setDetails(Arrays.asList(new CollectionItemDetail(collectionItem.getQty(),
						collectionItem.getCollectedQty(), collectionItem.getInventoryIn() != null, fboBranchName)));
			}
			Long branchId = collection.getBranchId();
			Optional<Branch> branchOptional = branchRepository.findById(branchId);
			if (branchOptional.isPresent()) {
				Branch branch = branchOptional.get();
				response.setBranchName(branch.getBranchName());
				response.setBranchId(branchId);
				Long warehouseId = branch.getWarehouseId();
				Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
				if (warehouseOptional.isPresent()) {
					Warehouse warehouse = warehouseOptional.get();
					String warehouseName = warehouse.getWarehouseName();
					response.setWarehouse(warehouseName);
				}
			}
			return response;
		}

		else if (type.equals(CollectionType.BULK)) {
			List<CollectionItem> requests = collection.getItems();
			Float totalQty = 0f;
			Integer collected = 0;
			Float totalCollectedQty = 0f;
			Integer inventoryInCreated = 0;

			List<CollectionItemDetail> details = new ArrayList<>();

			for (CollectionItem collectionItem : requests) {
				totalQty += collectionItem.getQty();
				if (collectionItem.getIsCollected()) {
					inventoryInCreated += collectionItem.getInventoryIn() != null ? 1 : 0;
					collected++;
					if (collectionItem.getCollectedQty() != null) {
						totalCollectedQty += collectionItem.getCollectedQty();
					}
				}

				Long fboBranchId = collectionItem.getFboBranchId();
				Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
				if (fboBranchOptional.isPresent()) {
					FBOBranch fboBranch = fboBranchOptional.get();
					CollectionItemDetail detail = new CollectionItemDetail(collectionItem.getQty(),
							collectionItem.getCollectedQty(), collectionItem.getInventoryIn() != null,
							fboBranch.getFboBranchName());
					details.add(detail);
				}
			}

			CollectionResponse response = new CollectionResponse();
			response.setId(collection.getId());
			response.setCollectionName(collection.getCollectionName());
			response.setRequestDate(requests.get(0).getRequestDate());
			response.setQty(totalQty);
			response.setIsAssigned(collection.getIsAssigned());
			Integer notCollected = 0;
			Integer partiallyCollected = 1;
			Integer fullyCollected = 2;
			response.setCollected(
					collected == 0 ? notCollected : collected == requests.size() ? fullyCollected : partiallyCollected);
			response.setType(collection.getCollectionType());
			response.setInventoryIn(inventoryInCreated == requests.size());
			response.setCollectedQty(totalCollectedQty);
			response.setDetails(details);

			Long branchId = collection.getBranchId();
			Optional<Branch> branchOptional = branchRepository.findById(branchId);
			if (branchOptional.isPresent()) {
				Branch branch = branchOptional.get();
				response.setBranchName(branch.getBranchName());
				response.setBranchId(branchId);
				Long warehouseId = branch.getWarehouseId();
				Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
				if (warehouseOptional.isPresent()) {
					Warehouse warehouse = warehouseOptional.get();
					String warehouseName = warehouse.getWarehouseName();
					response.setWarehouse(warehouseName);
				}
			}
			return response;
		}

		return null;
	}

	@Transactional
	public void createSingleCollection(CollectionItemRequest request) {
		Collection collection = new Collection();
		CollectionItem collectionItem = new CollectionItem();
		Long fboBranchId = request.getFboBranchId();
		Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
		if (fboBranchOptional.isPresent()) {
			FBOBranch fboBranch = fboBranchOptional.get();
			Long branchId = fboBranch.getBranchId();
			Long warehouseId = branchRepository.findById(branchId).map(Branch::getWarehouseId).orElse(null);
			collectionItem.setFboBranchId(fboBranchId);
			collectionItem.setRequestDate(request.getRequestDate());
			collectionItem.setQty(request.getQty());
			collectionItem.setDescription(request.getDescription());
			Optional<Rate> rateOptional = rateRepository.getRate(fboBranchId);
			if (rateOptional.isPresent()) {
				Rate rate = rateOptional.get();
				collectionItem.setRateId(rate.getId());
			}
			CustomUserDetails user = userService.getUser();
			String username = user.getUsername();
			collection.setCollectionName(fboBranch.getFboBranchName());
			collection.setCollectionType(CollectionType.SINGLE);
			collection.setAddedBy(username);
			collection.setBranchId(branchId);
			collection.setWarehouseId(warehouseId);
			collection = collectionRepository.save(collection);
			collectionItem.setCollection(collection);
			collectionItemRepository.save(collectionItem);
		}
	}

	@Transactional
	public void createBulkCollection(BulkCollecionRequest request) {
		CustomUserDetails user = userService.getUser();
		String username = user.getUsername();
		String collectionName = request.getCollectionName();
		Long branchId = request.getBranchId();
		List<CollectionItemRequest> items = request.getItems();
		Collection collection = new Collection();
		collection.setAddedBy(username);
		collection.setCollectionName(collectionName);
		collection.setCollectionType(CollectionType.BULK);
		collection.setBranchId(branchId);
		Optional<Branch> branchOptional = branchRepository.findById(branchId);
		if (branchOptional.isPresent()) {
			Branch branch = branchOptional.get();
			Long warehouseId = branch.getWarehouseId();
			collection.setWarehouseId(warehouseId);
		}
		collection = collectionRepository.save(collection);
		for (CollectionItemRequest item : items) {
			CollectionItem collectionItem = new CollectionItem();
			Long fboBranchId = item.getFboBranchId();
			Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
			if (fboBranchOptional.isPresent()) {
				collectionItem.setFboBranchId(fboBranchId);
				collectionItem.setRequestDate(item.getRequestDate());
				collectionItem.setQty(item.getQty());
				collectionItem.setDescription(item.getDescription());
				collectionItem.setCollection(collection);
				Optional<Rate> rateOptional = rateRepository.getRate(fboBranchId);
				if (rateOptional.isPresent()) {
					Rate rate = rateOptional.get();
					collectionItem.setRateId(rate.getId());
				}
				collectionItemRepository.save(collectionItem);
			}
		}
	}

	@Transactional
	public void assignPickupBoy(CollectionAssignRequest request) {
		Long collectionId = request.getCollectionId();
		Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
		if (collectionOptional.isPresent()) {
			Collection collection = collectionOptional.get();
			CustomUserDetails user = userService.getUser();
			collection.setPickupboy1(request.getUser1());
			collection.setPickupboy2(request.getUser2());
			collection.setAssignedBy(user.getUsername());
			collection.setIsAssigned(true);
			collection.setAssignedOn(LocalDateTime.now());
			collection.setTransporter(request.getTransporter());
			collectionRepository.save(collection);
		}
	}

	@Transactional
	public void collect(CollectionCollectRequest request) throws ParseException {
		if (request.getCollectionType().equals(CollectionType.SINGLE)) {
			Long collectionId = request.getCollectionId();
			Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
			if (collectionOptional.isPresent()) {
				Collection collection = collectionOptional.get();
				CustomUserDetails user = userService.getUser();
				String username = user.getUsername();
				List<CollectionItem> requests = collection.getItems();
				requests.forEach(r -> {
					r.setInvoiceNo(request.getInvoiceNo());
					r.setDescription(request.getDescr());
					r.setCollectedQty(request.getCollectedQty());
					r.setCollectedBy(username);
					r.setCollectedOn(request.getCollectedOn());
					r.setIsCollected(true);
					collectionItemRepository.save(r);
				});
			}
		}

		else if (request.getCollectionType().equals(CollectionType.BULK)) {
			Long requestId = request.getRequestId();
			Optional<CollectionItem> itemOptional = collectionItemRepository.findById(requestId);
			if (itemOptional.isPresent()) {
				CustomUserDetails user = userService.getUser();
				String username = user.getUsername();
				CollectionItem r = itemOptional.get();
				r.setInvoiceNo(request.getInvoiceNo());
				r.setDescription(request.getDescr());
				r.setCollectedQty(request.getCollectedQty());
				r.setCollectedBy(username);
				r.setCollectedOn(request.getCollectedOn());
				r.setIsCollected(true);
				collectionItemRepository.save(r);
			}
		}
	}

	@Transactional
	public CertficateResponse getCertificate(Long requestId) throws ParseException {
		Optional<CollectionItem> requestOptional = collectionItemRepository.findById(requestId);
		if (requestOptional.isPresent()) {
			CollectionItem request = requestOptional.get();
			Long fboBranchId = request.getFboBranchId();
			if (fboBranchId != null) {
				Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
				if (fboBranchOptional.isPresent()) {
					FBOBranch fboBranch = fboBranchOptional.get();
					String fboBranchName = fboBranch.getFboBranchName();
					String certificateNo = "1"; // request.getCertificateNo();
					String collectedOn = request.getCollectedOn();
					String invoiceNo = request.getInvoiceNo();
					Float collectedQty = request.getCollectedQty();
					CertficateResponse response = new CertficateResponse();
					response.setFbo_name(fboBranchName);
					response.setCertificate_no(certificateNo != null ? certificateNo.toUpperCase() : certificateNo);
					response.setInvoice_no(invoiceNo);
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(collectedOn);
					String collected = new SimpleDateFormat("dd-MM-yyyy").format(date);
					response.setCollected_on(collected);
					response.setCollected_qty(collectedQty);
					return response;
				}
			}
		}
		return null;
	}

	@Transactional
	public String getBulkCollectionName() {
		Optional<String> name = collectionRepository.getLatestCollectionName();
		return name.orElse("Collection 00001");
	}

	@Transactional
	public BulkCollectionResponse getBulk(Long collectionId) {
		Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
		if (collectionOptional.isPresent()) {
			Collection collection = collectionOptional.get();
			BulkCollectionResponse response = new BulkCollectionResponse();
			String collectionName = collection.getCollectionName();
			response.setCollectionName(collectionName);
			List<CollectionItem> requests = collection.getItems();
			List<CollectionItemResponse> items = requests.stream().map(r -> {
				CollectionItemResponse i = new CollectionItemResponse();
				i.setId(r.getId());
				i.setQty(r.getQty());
				i.setDate(r.getRequestDate());
				i.setDescription(r.getDescription());
				i.setCollectedStatus(r.getIsCollected() ? "Collected" : "Not Collected");
				Long fboBranchId = r.getFboBranchId();
				Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
				if (fboBranchOptional.isPresent()) {
					FBOBranch fboBranch = fboBranchOptional.get();
					String fboBranchName = fboBranch.getFboBranchName();
					i.setFboBranchName(fboBranchName);
				}
				return i;
			}).collect(Collectors.toList());
			response.setItems(items);
			return response;
		}
		return null;
	}

	@Transactional
	public void delete(Long id) {
		Optional<Collection> collectionOptional = collectionRepository.findById(id);
		if (collectionOptional.isPresent()) {
			Collection collection = collectionOptional.get();
			collection.setIsDelete(true);
			collection.getItems().forEach(r -> {
				r.setIsDelete(true);
				collectionItemRepository.save(r);
			});
			collectionRepository.save(collection);
		}
	}

	@Transactional
	public void upload(Long requestId, List<MultipartFile> files, Long collectionId, String type, Float collectedQty,
			String collectedOn, String invoiceNo, String descr) {
		Path uploadPath = Paths.get(filePath);
		List<Image> images = new ArrayList<>();

		try {

			if (files != null && files.size() > 0) {
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						String originalName = file.getOriginalFilename();
						String extension = "";
						if (originalName != null) {
							int dotIndex = originalName.lastIndexOf('.');
							if (dotIndex > 0) {
								extension = originalName.substring(dotIndex);
							}
						}

						else {
							extension = ".jpg";
						}

						String newFileName = System.currentTimeMillis() + extension;
						Path filePathFull = uploadPath.resolve(newFileName);
						Files.copy(file.getInputStream(), filePathFull);

						Image image = new Image();
						image.setFileName(newFileName);
						image.setRequestId(requestId);
						image = imageRepository.save(image);
						images.add(image);
					}
				}

			}

			if (type.equals(CollectionType.SINGLE)) {
				Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
				if (collectionOptional.isPresent()) {
					Collection collection = collectionOptional.get();
					CustomUserDetails user = userService.getUser();
					String username = user.getUsername();
					List<CollectionItem> requests = collection.getItems();
					requests.forEach(r -> {
						r.setInvoiceNo(invoiceNo);
						r.setDescription(descr);
						r.setCollectedQty(collectedQty);
						r.setCollectedBy(username);
						r.setCollectedOn(collectedOn);
						r.setImages(images);
						r.setIsCollected(true);
						collectionItemRepository.save(r);
					});
				}
			}

			else if (type.equals(CollectionType.BULK)) {
				Optional<CollectionItem> itemOptional = collectionItemRepository.findById(requestId);
				if (itemOptional.isPresent()) {
					CustomUserDetails user = userService.getUser();
					String username = user.getUsername();
					CollectionItem r = itemOptional.get();
					r.setInvoiceNo(invoiceNo);
					r.setDescription(descr);
					r.setCollectedQty(collectedQty);
					r.setCollectedBy(username);
					r.setCollectedOn(collectedOn);
					r.setImages(images);
					r.setIsCollected(true);
					collectionItemRepository.save(r);
				}
			}
		}

		catch (IOException e) {
			throw new RuntimeException("Failed to save files: " + e.getMessage(), e);
		}
	}

	@Transactional
	public void requestOTP() throws IOException, PostmarkException {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		CustomUserDetails user = userService.getUser();
		String to = user.getEmail();
		String subbject = "OTP to login";
		String body = "Your verification code: " + otp;
		mailService.sendPostMarkEmail(to, subbject, body);
		otpMap.put(otp, LocalDateTime.now());
	}

	@Transactional
	public Page<?> getAssignedCollection(Integer limit, Integer offset) {
		CustomUserDetails user = userService.getUser();
		Long branchId = user.getBranchId();
		Long userId = user.getId();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "id"));
		Page<Collection> page = collectionRepository
				.findAll(SearchBuilder.collectionHasSearchText(null, null, branchId, userId), pageable);
		return page.map(this::mapData);
	} 

	@Transactional
	public CollectionResponse getCollectionData(Long collectionId) {
		Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
		return collectionOptional.map(this::mapData).orElse(null);
	}

	@Transactional
	public void verifyOTP(Integer otp) {
		LocalDateTime time = otpMap.get(otp);
		if (time != null) {
			Duration duration = Duration.between(time, LocalDateTime.now());
			long minutes = duration.toMinutes();

			if (minutes < 10) {
				System.out.println("OTP is still valid");
			} else {
				System.out.println("OTP has expired");
			}
		}

		else {
			System.out.println("Invalid OTP");
		}
	}

	@Transactional
	public Page<?> getInventotyOut(Integer limit, Integer offset) {
		Pageable pageable = PageRequest.of(offset, limit, Sort.Direction.DESC, "saveDate");
		Page<InventoryOut> page = inventoryOutRepository.findAllByIsDeleteFalse(pageable);
		return page.map(this::mapData);
	}

	private InventoryOutResponse mapData(InventoryOut out) {
		Long branchId = out.getBranchId();
		Optional<Branch> branchOptional = branchRepository.findById(branchId);
		String branchName = "-";
		String customerName = "-";
		if (branchOptional.isPresent()) {
			Branch branch = branchOptional.get();
			branchName = branch.getBranchName();
		}
		Long customerId = out.getCustomerId();
		Optional<Customer> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customerName = customer.getCustomerName();
		}
		InventoryOutResponse response = new InventoryOutResponse();
		response.setId(out.getId());
		response.setAddedBy(out.getAddedBy());
		response.setBranchName(branchName);
		response.setCustomerName(customerName);
		response.setQty(out.getQty());
		response.setComment(out.getComment());
		response.setVehicleNo(out.getVehicleNo());
		response.setTransporter(out.getTransporter());
		return response;
	}

	@Transactional
	public void createInventoryOut(InventoryOutRequest request) {
		Long branchId = request.getBranchId();
		List<InventoryIn> inventoryIns = inventoryInRepository.findAllByBranchIdAndIsDeleteFalse(branchId);
		List<Long> ids = new ArrayList<>();
		inventoryIns.forEach(in -> {
			in.setIsDelete(true);
			ids.add(in.getId());
			inventoryInRepository.save(in);
		});
		CustomUserDetails user = userService.getUser();
		InventoryOut out = new InventoryOut();
		out.setAddedBy(user.getUsername());
		out.setBranchId(branchId);
		out.setCustomerId(request.getCustomerId());
		out.setQty(request.getQty());
		out.setComment(request.getComment());
		out.setVehicleNo(request.getVehicleNo());
		out.setTransporter(request.getTransporter());
		out.setIds(ids.stream().map(String::valueOf).collect(Collectors.joining(",")));
		inventoryOutRepository.save(out);
	}

	@Transactional
	public void deleteInventoryOut(Long id) {
		Optional<InventoryOut> outOptional = inventoryOutRepository.findById(id);
		if (outOptional.isPresent()) {
			InventoryOut out = outOptional.get();
			out.setIsDelete(true);

			String ids = out.getIds();
			List<Long> inventoryInIds = Arrays.asList(ids.split(",")).stream().map(Long::parseLong)
					.collect(Collectors.toList());
			inventoryInRepository.resetStatus(inventoryInIds);

			inventoryOutRepository.save(out);

		}
	}

	@Transactional
	public Page<?> getInventotyIn(Integer limit, Integer offset) {
		Pageable pageable = PageRequest.of(offset, limit, Sort.Direction.DESC, "saveDate");
		Page<InventoryIn> page = inventoryInRepository.findAll(pageable);
		return page.map(this::mapData);
	}

	private InventoryInResponse mapData(InventoryIn in) {
		Long branchId = in.getBranchId();
		Optional<Branch> branchOptional = branchRepository.findById(branchId);
		String branchName = "-";
		String warehouseName = "-";
		if (branchOptional.isPresent()) {
			Branch branch = branchOptional.get();
			branchName = branch.getBranchName();
			Long warehouseId = branch.getWarehouseId();
			Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
			if (warehouseOptional.isPresent()) {
				Warehouse warehouse = warehouseOptional.get();
				warehouseName = warehouse.getWarehouseName();
			}
		}
		InventoryInResponse response = new InventoryInResponse();
		response.setBranchName(branchName);
		response.setWarehouse(warehouseName);
		response.setQty(in.getInwardQty());
		response.setDate(in.getSaveDate());
		Long collectionItemId = in.getCollectionItemId();
		Optional<Float> rateOptional = collectionItemRepository.getRate(collectionItemId);
		if (rateOptional.isPresent()) {
			Float rate = rateOptional.get();
			response.setRate(rate);
			response.setTotal(in.getInwardQty() * rate);
		}
		return response;
	}

	@Transactional
	public void createInventoryIn(Long collectionId) {
		Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
		if (collectionOptional.isPresent()) {
			Collection collection = collectionOptional.get();
			List<CollectionItem> collectionItems = collection.getItems();
			collectionItems.forEach(collectionItem -> {
				if (collectionItem.getIsCollected() && collectionItem.getCollectedQty() > 0
						&& collectionItem.getInventoryIn() == null) {
					InventoryIn in = new InventoryIn();
					in.setCollectionItemId(collectionItem.getId());
					in.setBranchId(collection.getBranchId());
					in.setInwardQty(collectionItem.getCollectedQty());
					in = inventoryInRepository.save(in);
					collectionItem.setInventoryIn(in);
					collectionItemRepository.save(collectionItem);
				}
			});
		}

	}

}
