package com.ruco.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ruco.config.CustomUserDetails;
import com.ruco.constant.FBOUserType;
import com.ruco.constant.UserType;
import com.ruco.model.FBO;
import com.ruco.model.FBOBranch;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CollectionRepository;
import com.ruco.repo.CompanyRepository;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.FboRepository;
import com.ruco.repo.InventoryInRepository;
import com.ruco.repo.InventoryOutRepository;
import com.ruco.repo.TicketRepository;
import com.ruco.repo.TransporterRepository;
import com.ruco.response.DashboardResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final CompanyRepository companyRepository;
	private final BranchRepository branchRepository;
	private final FboRepository fboRepository;
	private final TransporterRepository transporterRepository;
	private final CollectionRepository collectionRepository;
	private final UserService userService;
	private final FboBranchRepository fboBranchRepository;
	private final InventoryInRepository inventoryInRepository;
	private final InventoryOutRepository inventoryOutRepository;
	private final TicketRepository ticketRepository;

	private ExecutorService executor = Executors.newFixedThreadPool(6);

	public List<DashboardResponse> getData() {
		CustomUserDetails user = userService.getUser();
		Boolean isFboUser = user.getIsFboUser();
		String userType = user.getUserType();

		if (isFboUser) {
			Long fboId = user.getFboId();
			if (userType.equals(FBOUserType.FBO_SUPERADMIN)) {
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<Long> fboBranchIds = fbo.getFboBranches().stream().map(FBOBranch::getId)
							.collect(Collectors.toList());
					List<Long> branchIds = fbo.getFboBranches().stream().map(FBOBranch::getBranchId)
							.collect(Collectors.toList());
					if (branchIds == null) {
						branchIds = new ArrayList<>();
					}
					return fboUser(fboId, fboBranchIds, branchIds);
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
					List<Long> branchIds = fbo.getFboBranches().stream().filter(
							fboBranch -> fboBranch.getStateId() != null && fboBranch.getStateId().equals(userStateId))
							.map(FBOBranch::getBranchId).collect(Collectors.toList());
					if (branchIds == null) {
						branchIds = new ArrayList<>();
					}
					return fboUser(fboId, fboBranchIds, branchIds);
				}
			}

			else if (userType.equals(FBOUserType.FBO_CITY)) {
				Long userStateId = user.getStateId();
				Long userCityId = user.getCityId();
				Optional<FBO> fboOptional = fboRepository.findById(fboId);
				if (fboOptional.isPresent()) {
					FBO fbo = fboOptional.get();
					List<FBOBranch> validFboBranches = fbo.getFboBranches().stream()
							.filter(fboBranch -> fboBranch.getStateId() != null
									&& fboBranch.getStateId().equals(userStateId) && fboBranch.getCityId() != null
									&& fboBranch.getCityId().equals(userCityId))
							.collect(Collectors.toList());

					List<Long> fboBranchIds = validFboBranches.stream().map(FBOBranch::getId)
							.collect(Collectors.toList());

					List<Long> branchIds = validFboBranches.stream().map(FBOBranch::getBranchId)
							.collect(Collectors.toList());

					return fboUser(fboId, fboBranchIds, branchIds);
				}
			}

			else if (userType.equals(FBOUserType.FBO_BRANCH)) {
				Long fboBranchId = user.getFboBranchId();
				Optional<FBOBranch> fboBranchOptional = fboBranchRepository.findById(fboBranchId);
				if (fboBranchOptional.isPresent()) {
					FBOBranch fboBranch = fboBranchOptional.get();
					Long branchId = fboBranch.getBranchId();
					return fboUser(fboId, Arrays.asList(fboBranchId), Arrays.asList(branchId));
				}
			}

		}

		else {

			if (userType.equals(UserType.MANAGER)) {
				Long branchId = user.getBranchId();
				return supervisorDashboardData(branchId);
			}

			else if (userType.equals(UserType.PICKUPBOY)) {
				Long branchId = user.getBranchId();
				Long userId = user.getId();
				return pickupboyDashboardData(branchId, userId);
			}

			else if (userType.equals(UserType.SUPERADMIN)) {
				return adminData();
			}
		}

		return null;

	}

	private List<DashboardResponse> fboUser(Long fboId, List<Long> fboBranchIds, List<Long> branchIds) {
		try {
			CompletableFuture<Integer> branchesFuture = CompletableFuture
					.supplyAsync(() -> branchRepository.findAllByIdInAndIsDeleteFalse(branchIds).size(), executor);
			CompletableFuture<Integer> fbosFuture = CompletableFuture
					.supplyAsync(() -> fboRepository.findByIdAndIsDeleteFalse(fboId).isPresent() ? 1 : 0, executor);
			CompletableFuture<Integer> collectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getTotalCollections(fboBranchIds).orElse(0), executor);
			CompletableFuture<Integer> notAssignedCollectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getNotAssignedCount(fboBranchIds).orElse(0), executor);
			CompletableFuture<Integer> notCollectedCollectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getNotCollectedCount(fboBranchIds).orElse(0), executor);
			CompletableFuture<Float> collectedQtyFuture = CompletableFuture.supplyAsync(() -> {
				Float collectedQty = inventoryInRepository.getCollectedQtyCountByBranchIds(branchIds).orElse(0f);
				Float outwardQty = inventoryOutRepository.getOutwardQtyByBranchIds(branchIds).orElse(0f);
				return collectedQty - outwardQty;
			}, executor);
			CompletableFuture<Integer> openTicketFuture = CompletableFuture
					.supplyAsync(() -> ticketRepository.getOpenTicketCount().orElse(0), executor);

			CompletableFuture.allOf(branchesFuture, fbosFuture, collectionsFuture, notAssignedCollectionsFuture,
					notCollectedCollectionsFuture).join();

			DashboardResponse branch = new DashboardResponse("Branches", "branch/list", branchesFuture.get(),
					"images/briefcase.svg", "gradient-2");
			DashboardResponse fbo = new DashboardResponse("FBOs", "fbo/list", fbosFuture.get(), "images/drop.svg",
					"gradient-3");
			DashboardResponse collection = new DashboardResponse("Collections", "collection/list",
					collectionsFuture.get(), "images/dcollection.svg", "gradient-7");
			DashboardResponse notAssignedCollection = new DashboardResponse("Not Assigned Collections",
					"collection/list", notAssignedCollectionsFuture.get(), "images/dcollection.svg", "gradient-8");
			DashboardResponse notCollectedCollection = new DashboardResponse("Not Collected Collections",
					"collection/list", notCollectedCollectionsFuture.get(), "images/dcollection.svg", "gradient-1");
			DashboardResponse collectedQty = new DashboardResponse("Inventory Qty", "", collectedQtyFuture.get(),
					"images/dcollection.svg", "gradient-1");
			DashboardResponse openTicket = new DashboardResponse("Open Tickets", "ticket/list", openTicketFuture.get(),
					"images/briefcase.svg", "gradient-5");

			return Arrays.asList(branch, fbo, collection, notAssignedCollection, notCollectedCollection, collectedQty,
					openTicket);

		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Error fetching dashboard data", e);
		}
	}

	private List<DashboardResponse> supervisorDashboardData(Long branchId) {
		try {
			CompletableFuture<Integer> branchesFuture = CompletableFuture
					.supplyAsync(() -> branchRepository.getBranchCount(branchId).orElse(0), executor);
			CompletableFuture<Integer> fbosFuture = CompletableFuture
					.supplyAsync(() -> fboRepository.getFboCount(branchId).orElse(0), executor);
			CompletableFuture<Integer> transportersFuture = CompletableFuture
					.supplyAsync(() -> transporterRepository.getTransporterCount(branchId).orElse(0), executor);
			CompletableFuture<Integer> collectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getCollectionCount(branchId).orElse(0), executor);
			CompletableFuture<Integer> notAssignedCollectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getNotAssignedCount(branchId).orElse(0), executor);
			CompletableFuture<Integer> notCollectedCollectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getNotCollectedCount(branchId).orElse(0), executor);
			CompletableFuture<Float> collectedQtyFuture = CompletableFuture
					.supplyAsync(() -> inventoryInRepository.getCollectedQtyCountByBranchId(branchId).orElse(0f)
							- inventoryOutRepository.getOutwardQtyByBranchId(branchId).orElse(0f), executor);
			CompletableFuture<Integer> openTicketFuture = CompletableFuture
					.supplyAsync(() -> ticketRepository.getOpenTicketCount().orElse(0), executor);

			CompletableFuture.allOf(branchesFuture, fbosFuture, transportersFuture, collectionsFuture,
					notAssignedCollectionsFuture, notCollectedCollectionsFuture, collectedQtyFuture).join();

			DashboardResponse branch = new DashboardResponse("Branches", "branch/list", branchesFuture.get(),
					"images/briefcase.svg", "gradient-2");
			DashboardResponse fbo = new DashboardResponse("FBOs", "fbo/list", fbosFuture.get(), "images/drop.svg",
					"gradient-3");
			DashboardResponse transporter = new DashboardResponse("Transporters", "transporter/list",
					transportersFuture.get(), "images/d-transporter.svg", "gradient-5");
			DashboardResponse collection = new DashboardResponse("Collections", "collection/list",
					collectionsFuture.get(), "images/dcollection.svg", "gradient-7");
			DashboardResponse notAssignedCollection = new DashboardResponse("Not Assigned Collections",
					"collection/list", notAssignedCollectionsFuture.get(), "images/dcollection.svg", "gradient-8");
			DashboardResponse notCollectedCollection = new DashboardResponse("Not Collected Collections",
					"collection/list", notCollectedCollectionsFuture.get(), "images/dcollection.svg", "gradient-1");
			DashboardResponse collectedQty = new DashboardResponse("Inventory Qty", "", collectedQtyFuture.get(),
					"images/dcollection.svg", "gradient-1");
			DashboardResponse openTicket = new DashboardResponse("Open Tickets", "ticket/list", openTicketFuture.get(),
					"images/briefcase.svg", "gradient-5");
			return Arrays.asList(branch, fbo, transporter, collection, notAssignedCollection, notCollectedCollection,
					collectedQty, openTicket);

		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Error fetching dashboard data", e);
		}
	}

	private List<DashboardResponse> pickupboyDashboardData(Long branchId, Long userId) {
		try {
			CompletableFuture<Integer> collectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getCollectionCount(branchId, userId).orElse(0), executor);
			CompletableFuture<Integer> notCollectedCollectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getNotCollectedCount(branchId, userId).orElse(0), executor);
			CompletableFuture<Float> collectedQtyFuture = CompletableFuture
					.supplyAsync(() -> inventoryInRepository.getCollectedQtyCountByBranchId(branchId).orElse(0f)
							- inventoryOutRepository.getOutwardQtyByBranchId(branchId).orElse(0f), executor);

			CompletableFuture.allOf(collectionsFuture, notCollectedCollectionsFuture, collectedQtyFuture).join();

			DashboardResponse collection = new DashboardResponse("Collections", "collection/list",
					collectionsFuture.get(), "images/dcollection.svg", "gradient-7");
			DashboardResponse notCollectedCollection = new DashboardResponse("Not Collected Collections",
					"collection/list", notCollectedCollectionsFuture.get(), "images/dcollection.svg", "gradient-1");

			return Arrays.asList(collection, notCollectedCollection);

		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Error fetching dashboard data", e);
		}
	}

	private List<DashboardResponse> adminData() {
		try {
			CompletableFuture<Integer> companiesFuture = CompletableFuture
					.supplyAsync(() -> companyRepository.findAllByIsDeleteFalse().size(), executor);
			CompletableFuture<Integer> branchesFuture = CompletableFuture
					.supplyAsync(() -> branchRepository.findAllByIsDeleteFalse().size(), executor);
			CompletableFuture<Integer> fbosFuture = CompletableFuture
					.supplyAsync(() -> fboRepository.findAllByIsDeleteFalse().size(), executor);
			CompletableFuture<Integer> transportersFuture = CompletableFuture
					.supplyAsync(() -> transporterRepository.findAllByIsDeleteFalse().size(), executor);
			CompletableFuture<Integer> collectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.findAllByIsDeleteFalse().size(), executor);
			CompletableFuture<Integer> notAssignedCollectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getNotAssignedCount().orElse(0), executor);
			CompletableFuture<Integer> notCollectedCollectionsFuture = CompletableFuture
					.supplyAsync(() -> collectionRepository.getNotCollectedCount().orElse(0), executor);
			CompletableFuture<Float> collectedQtyFuture = CompletableFuture
					.supplyAsync(() -> inventoryInRepository.getTotalCollectedQtyCount().orElse(0f)
							- inventoryOutRepository.getTotalOutwardQty().orElse(0f), executor);
			CompletableFuture<Integer> openTicketFuture = CompletableFuture
					.supplyAsync(() -> ticketRepository.getOpenTicketCount().orElse(0), executor);

			CompletableFuture.allOf(companiesFuture, branchesFuture, fbosFuture, transportersFuture, collectionsFuture,
					notAssignedCollectionsFuture, notCollectedCollectionsFuture, collectedQtyFuture).join();

			DashboardResponse company = new DashboardResponse("Companies", "company/list", companiesFuture.get(),
					"images/d-company.svg", "gradient-1");
			DashboardResponse branch = new DashboardResponse("Branches", "branch/list", branchesFuture.get(),
					"images/briefcase.svg", "gradient-2");
			DashboardResponse fbo = new DashboardResponse("FBOs", "fbo/list", fbosFuture.get(), "images/drop.svg",
					"gradient-3");
			DashboardResponse transporter = new DashboardResponse("Transporters", "transporter/list",
					transportersFuture.get(), "images/d-transporter.svg", "gradient-5");
			DashboardResponse collection = new DashboardResponse("Collections", "collection/list",
					collectionsFuture.get(), "images/dcollection.svg", "gradient-7");
			DashboardResponse notAssignedCollection = new DashboardResponse("Not Assigned Collections",
					"collection/list", notAssignedCollectionsFuture.get(), "images/dcollection.svg", "gradient-8");
			DashboardResponse notCollectedCollection = new DashboardResponse("Not Collected Collections",
					"collection/list", notCollectedCollectionsFuture.get(), "images/dcollection.svg", "gradient-1");
			DashboardResponse collectedQty = new DashboardResponse("Inventory Qty", "", collectedQtyFuture.get(),
					"images/dcollection.svg", "gradient-1");
			DashboardResponse openTicket = new DashboardResponse("Open Tickets", "ticket/list", openTicketFuture.get(),
					"images/briefcase.svg", "gradient-5");
			return Arrays.asList(company, branch, fbo, transporter, collection, notAssignedCollection,
					notCollectedCollection, collectedQty, openTicket);

		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Error fetching dashboard data", e);
		}
	}

}
