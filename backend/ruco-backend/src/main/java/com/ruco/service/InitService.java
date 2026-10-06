package com.ruco.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ruco.constant.CustomerType;
import com.ruco.constant.FBOAccountType;
import com.ruco.constant.UserType;
import com.ruco.model.Area;
import com.ruco.model.Branch;
import com.ruco.model.City;
import com.ruco.model.Company;
import com.ruco.model.Customer;
import com.ruco.model.FBO;
import com.ruco.model.FBOBranch;
import com.ruco.model.FBOBranchLogin;
import com.ruco.model.State;
import com.ruco.model.Transporter;
import com.ruco.model.User;
import com.ruco.model.Warehouse;
import com.ruco.repo.AreaRepository;
import com.ruco.repo.BranchRepository;
import com.ruco.repo.CityRepository;
import com.ruco.repo.CompanyRepository;
import com.ruco.repo.CustomerRepository;
import com.ruco.repo.FboBranchLoginRepository;
import com.ruco.repo.FboBranchRepository;
import com.ruco.repo.FboRepository;
import com.ruco.repo.StateRepository;
import com.ruco.repo.TransporterRepository;
import com.ruco.repo.UserRepository;
import com.ruco.repo.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InitService {

	private final CompanyRepository companyRepository;
	private final BranchRepository branchRepository;
	private final TransporterRepository transporterRepository;
	private final UserRepository userRepository;
	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final AreaRepository areaRepository;
	private final FboRepository fboRepository;
	private final FboBranchRepository fboBranchRepository;
	private final FboBranchLoginRepository fboBranchLoginRepository;
	private final PasswordEncoder encoder;
	private final WarehouseRepository warehouseRepository;
	private final CustomerRepository customerRepository;

	@PostConstruct
	void transferData() throws SQLException {
		insertState();
		insertCity();
		insertArea();
		insertBranch();
		insertUser();
		insertTransporter();
		insertFbo();
		insertFboBranch();
		insertFboBranchLogin();
		insertCustomer();
		insertSuperAdmin();
		insertCompany();
	}

	void insertSuperAdmin() {
		if (userRepository.findAll().size() == 0) {
			String password = "1";
			String encodedPassword = encoder.encode(password);

			User user1 = new User();
			user1.setLoginId("superadmin");
			user1.setPassword(encodedPassword);
			user1.setUsername("Super Admin");
			user1.setEmail("superadmin@gmail.com");
			user1.setMobileNo(9999999999L);
			user1.setUserType(UserType.SUPERADMIN);
			userRepository.save(user1);
		}
	}

	void insertCustomer() {
		if (customerRepository.findAll().size() == 0) {
			Customer customer = new Customer();
			customer.setCustomerName("Kaleesuwari Refinery and Industry Pvt. Ltd.");
			customer.setCustomerAddress("Phase-3, Industrial Park, Vakalapudi, Kakinada Rural");
			customer.setCustomerType(CustomerType.PLANT);
			customer.setEmail("kaleesuwari_refinery@gmail.com");
			customer.setMobileNo(9999999999l);
			customerRepository.save(customer);

			customer = new Customer();
			customer.setCustomerName("Pure Reuse Solutions");
			customer.setCustomerAddress("Plot No. 45, GIDC Vatva, Ahmedabad, Gujarat – 382445");
			customer.setCustomerType(CustomerType.PLANT);
			customer.setEmail("ramesh.patel@purereuse.in");
			customer.setMobileNo(9123456789l);
			customerRepository.save(customer);

			customer = new Customer();
			customer.setCustomerName("EcoCycle Oil Recovery");
			customer.setCustomerAddress("202A, Green Valley Complex, Andheri West, Mumbai, Maharashtra – 400058");
			customer.setCustomerType(CustomerType.PLANT);
			customer.setEmail("sunita.sharma@ecocyclerecovery.com");
			customer.setMobileNo(9987654321l);
			customerRepository.save(customer);

			customer = new Customer();
			customer.setCustomerName("Green Earth Biofuels");
			customer.setCustomerAddress("17/2 Industrial Estate, Phase II, Peenya, Bengaluru, Karnataka – 560058");
			customer.setCustomerType(CustomerType.PLANT);
			customer.setEmail("anil.verma@greenearthbiofuels.in");
			customer.setMobileNo(9876543210l);
			customerRepository.save(customer);
		}
	}

	void insertFbo() {
		if (fboRepository.findAll().size() == 0) {
			FBO fbo1 = new FBO();
			fbo1.setFboName("Devyani International LTD");
			fbo1.setBankName("Yes Bank LTD");
			fbo1.setAccountType(FBOAccountType.CURRENT_ACCOUNT);
			fbo1.setAccountNo("711230110000105");
			fbo1.setAccountHolderName("JITENDRA KIRTI");
			fbo1.setIfscCode("BK1D0007112");
			fbo1.setMobileNo(9999273850L);
			fboRepository.save(fbo1);

			FBO fbo2 = new FBO();
			fbo2.setFboName("Barbeque Nation Hospitality Ltd");
			fbo2.setBankName("HDFC Bank");
			fbo2.setAccountType(FBOAccountType.CURRENT_ACCOUNT);
			fbo2.setAccountNo("50200045811234");
			fbo2.setAccountHolderName("RAHUL MEHTA");
			fbo2.setIfscCode("HDFC0001452");
			fbo2.setMobileNo(9876543210L);
			fboRepository.save(fbo2);

			FBO fbo3 = new FBO();
			fbo3.setFboName("Biryani Blues Pvt Ltd");
			fbo3.setBankName("ICICI Bank");
			fbo3.setAccountType(FBOAccountType.CURRENT_ACCOUNT);
			fbo3.setAccountNo("00450120034876");
			fbo3.setAccountHolderName("PRIYA SINGH");
			fbo3.setIfscCode("ICIC0000451");
			fbo3.setMobileNo(9123456789L);
			fboRepository.save(fbo3);

			FBO fbo4 = new FBO();
			fbo4.setFboName("Paradise Food Court");
			fbo4.setBankName("Axis Bank");
			fbo4.setAccountType(FBOAccountType.CURRENT_ACCOUNT);
			fbo4.setAccountNo("91301005321234");
			fbo4.setAccountHolderName("ARJUN REDDY");
			fbo4.setIfscCode("UTIB0001212");
			fbo4.setMobileNo(9812345678L);
			fboRepository.save(fbo4);

			FBO fbo5 = new FBO();
			fbo5.setFboName("Behrouz Biryani - Rebel Foods");
			fbo5.setBankName("Kotak Mahindra Bank");
			fbo5.setAccountType(FBOAccountType.CURRENT_ACCOUNT);
			fbo5.setAccountNo("19876543210987");
			fbo5.setAccountHolderName("KAVITA JOSHI");
			fbo5.setIfscCode("KKBK0001209");
			fbo5.setMobileNo(9934567890L);
			fboRepository.save(fbo5);

		}

	}

	void insertFboBranch() {
		if (fboBranchRepository.findAll().size() == 0) {
			Optional<FBO> fboOptional = fboRepository.findById(1l);
			if (fboOptional.isPresent()) {
				FBO fbo = fboOptional.get();
				FBOBranch fboBranch1 = new FBOBranch();
				fboBranch1.setFboBranchName("Devyani International LTD - TamilNadu Branch");
				fboBranch1.setFboBranchAddress("Chennai TamilNadu");
				fboBranch1.setRepresentative("Mohan singh");
				fboBranch1.setStateId(23l);
				fboBranch1.setCityId(24l);
				fboBranch1.setEmail("Kfc.hudametrogurugram@dil-rjcorp.com");
				fboBranch1.setMobileNo(8587963948l);
				fboBranch1.setGstNo("09AABCD5534A1Z3");
				fboBranch1.setFssaiNo("10822005001643");
				fboBranch1.setFbo(fbo);
				fboBranch1.setBranchId(1l);
				fboBranchRepository.save(fboBranch1);

				FBOBranch fboBranch2 = new FBOBranch();
				fboBranch2.setFboBranchName("Devyani International LTD -  Karnataka Branch");
				fboBranch2.setFboBranchAddress("Koramangala Karnataka");
				fboBranch2.setRepresentative("Mohan singh");
				fboBranch2.setStateId(11l);
				fboBranch2.setCityId(32l);
				fboBranch2.setBranchId(4l);
				fboBranch2.setEmail("kfc.grandvenicemallgrnoida@dil-rjcorp.com");
				fboBranch2.setMobileNo(7017245467l);
				fboBranch2.setGstNo("09AABCD5534A1Z3");
				fboBranch2.setFssaiNo("12722055001524");
				fboBranch2.setFbo(fbo);
				fboBranchRepository.save(fboBranch2);

				FBOBranch fboBranch3 = new FBOBranch();
				fboBranch3.setFboBranchName("Devyani International LTD - Maharashtra Branch");
				fboBranch3.setFboBranchAddress("Mumbai Maharashtra");
				fboBranch3.setRepresentative("Mohan singh");
				fboBranch3.setStateId(14l);
				fboBranch3.setCityId(47l);
				fboBranch3.setBranchId(2l);
				fboBranch3.setEmail("Kfc.anthuriumsec73noida@dil-rjcorp.com");
				fboBranch3.setMobileNo(9540533699l);
				fboBranch3.setGstNo("09AABCD5534A1Z3");
				fboBranch3.setFssaiNo("12722055001524");
				fboBranch3.setFbo(fbo);
				fboBranchRepository.save(fboBranch3);
			}
		}

	}

	void insertFboBranchLogin() {
		if (fboBranchLoginRepository.findAll().size() == 0) {
			Optional<FBO> fboOptional = fboRepository.findById(1l);
			if (fboOptional.isPresent()) {
				FBO fbo = fboOptional.get();

				FBOBranchLogin fboBranchLogin1 = new FBOBranchLogin();
				fboBranchLogin1.setEmail("kfc.ambi01@bluestone");
				fboBranchLogin1.setPassword(encoder.encode("1"));
				fboBranchLogin1.setPwd("1");
				fboBranchLogin1.setUsername("Riyaz Malik");
				fboBranchLogin1.setMobileNo(9696505588l);
				fboBranchLogin1.setType("FBO_SUPERADMIN");
				fboBranchLogin1.setFbo(fbo);
				fboBranchLoginRepository.save(fboBranchLogin1);

				FBOBranchLogin fboBranchLogin2 = new FBOBranchLogin();
				fboBranchLogin2.setEmail("Kfc.grnoida@dil-rjcorp.com");
				fboBranchLogin2.setPassword(encoder.encode("1"));
				fboBranchLogin2.setPwd("1");
				fboBranchLogin2.setUsername("kushwah");
				fboBranchLogin2.setMobileNo(9910885473l);
				fboBranchLogin2.setType("FBO_STATE");
				fboBranchLogin2.setFbo(fbo);
				fboBranchLogin2.setStateId(23l);
				fboBranchLoginRepository.save(fboBranchLogin2);

				FBOBranchLogin fboBranchLogin3 = new FBOBranchLogin();
				fboBranchLogin3.setEmail("kfcnoida18@dil-rjcorp.com");
				fboBranchLogin3.setPassword(encoder.encode("1"));
				fboBranchLogin3.setPwd("1");
				fboBranchLogin3.setUsername("Ashish");
				fboBranchLogin3.setMobileNo(9696505586l);
				fboBranchLogin3.setType("FBO_CITY");
				fboBranchLogin3.setFbo(fbo);
				fboBranchLogin3.setStateId(23l);
				fboBranchLogin3.setCityId(24l);
				fboBranchLoginRepository.save(fboBranchLogin3);

				FBOBranchLogin fboBranchLogin4 = new FBOBranchLogin();
				fboBranchLogin4.setEmail("Kfc.northeye01@bluestone");
				fboBranchLogin4.setPassword(encoder.encode("1"));
				fboBranchLogin4.setPwd("1");
				fboBranchLogin4.setUsername("Jogi");
				fboBranchLogin4.setMobileNo(9971800462l);
				fboBranchLogin4.setType("FBO_BRANCH");
				fboBranchLogin4.setFboBranchId(1l);
				fboBranchLogin4.setFbo(fbo);
				fboBranchLoginRepository.save(fboBranchLogin4);

			}
		}

	}

	void insertState() {
		if (stateRepository.findAll().size() == 0) {
			List<String> indianStatesAndUTs = Arrays.asList(
					// States
					"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat",
					"Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra",
					"Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim",
					"Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",

					// Union Territories
					"Andaman and Nicobar Islands", "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi",
					"Jammu and Kashmir", "Ladakh", "Lakshadweep", "Puducherry");
			List<State> states = indianStatesAndUTs.stream().map(stateName -> {
				State state = new State();
				state.setStateName(stateName);
				return state;
			}).collect(Collectors.toList());

			stateRepository.saveAll(states);
		}

	}

	void insertCity() {
		if (cityRepository.findAll().isEmpty()) {
			Map<String, List<String>> stateCityMap = new HashMap<>();

			// Sample cities per state
			stateCityMap.put("Andhra Pradesh", Arrays.asList("Visakhapatnam", "Vijayawada", "Guntur"));
			stateCityMap.put("Arunachal Pradesh", Arrays.asList("Itanagar", "Naharlagun"));
			stateCityMap.put("Assam", Arrays.asList("Guwahati", "Silchar", "Dibrugarh"));
			stateCityMap.put("Bihar", Arrays.asList("Patna", "Gaya", "Muzaffarpur"));
			stateCityMap.put("Chhattisgarh", Arrays.asList("Raipur", "Bilaspur", "Durg"));
			stateCityMap.put("Goa", Arrays.asList("Panaji", "Margao"));
			stateCityMap.put("Gujarat", Arrays.asList("Ahmedabad", "Surat", "Vadodara"));
			stateCityMap.put("Haryana", Arrays.asList("Gurugram", "Faridabad", "Panipat"));
			stateCityMap.put("Himachal Pradesh", Arrays.asList("Shimla", "Manali"));
			stateCityMap.put("Jharkhand", Arrays.asList("Ranchi", "Jamshedpur", "Dhanbad"));
			stateCityMap.put("Karnataka", Arrays.asList("Bengaluru", "Mysuru", "Mangaluru"));
			stateCityMap.put("Kerala", Arrays.asList("Thiruvananthapuram", "Kochi", "Kozhikode"));
			stateCityMap.put("Madhya Pradesh", Arrays.asList("Bhopal", "Indore", "Jabalpur"));
			stateCityMap.put("Maharashtra", Arrays.asList("Mumbai", "Pune", "Nagpur"));
			stateCityMap.put("Manipur", Arrays.asList("Imphal"));
			stateCityMap.put("Meghalaya", Arrays.asList("Shillong"));
			stateCityMap.put("Mizoram", Arrays.asList("Aizawl"));
			stateCityMap.put("Nagaland", Arrays.asList("Kohima", "Dimapur"));
			stateCityMap.put("Odisha", Arrays.asList("Bhubaneswar", "Cuttack", "Puri"));
			stateCityMap.put("Punjab", Arrays.asList("Ludhiana", "Amritsar", "Chandigarh"));
			stateCityMap.put("Rajasthan", Arrays.asList("Jaipur", "Udaipur", "Jodhpur"));
			stateCityMap.put("Sikkim", Arrays.asList("Gangtok"));
			stateCityMap.put("Tamil Nadu", Arrays.asList("Chennai", "Coimbatore", "Madurai"));
			stateCityMap.put("Telangana", Arrays.asList("Hyderabad", "Warangal"));
			stateCityMap.put("Tripura", Arrays.asList("Agartala"));
			stateCityMap.put("Uttar Pradesh", Arrays.asList("Lucknow", "Kanpur", "Varanasi"));
			stateCityMap.put("Uttarakhand", Arrays.asList("Dehradun", "Haridwar"));
			stateCityMap.put("West Bengal", Arrays.asList("Kolkata", "Howrah", "Durgapur"));

			// Union Territories
			stateCityMap.put("Andaman and Nicobar Islands", Arrays.asList("Port Blair"));
			stateCityMap.put("Chandigarh", Arrays.asList("Chandigarh"));
			stateCityMap.put("Dadra and Nagar Haveli and Daman and Diu", Arrays.asList("Silvassa", "Daman", "Diu"));
			stateCityMap.put("Delhi", Arrays.asList("New Delhi"));
			stateCityMap.put("Jammu and Kashmir", Arrays.asList("Srinagar", "Jammu"));
			stateCityMap.put("Ladakh", Arrays.asList("Leh", "Kargil"));
			stateCityMap.put("Lakshadweep", Arrays.asList("Kavaratti"));
			stateCityMap.put("Puducherry", Arrays.asList("Puducherry", "Karaikal"));

			List<City> cities = new ArrayList<>();

			for (Map.Entry<String, List<String>> entry : stateCityMap.entrySet()) {
				String stateName = entry.getKey();
				List<String> cityNames = entry.getValue();

				// Find state by name
				Optional<State> stateOptional = stateRepository.findByStateName(stateName);
				if (stateOptional != null) {
					State state = stateOptional.get();
					for (String cityName : cityNames) {
						City city = new City();
						city.setCityName(cityName);
						city.setState(state);
						cities.add(city);
					}
				}
			}

			cityRepository.saveAll(cities);
		}
	}

	void insertArea() {
		if (areaRepository.findAll().isEmpty()) {

			Map<String, List<String>> cityAreaMap = new HashMap<>();

			// Sample areas per city
			cityAreaMap.put("Mumbai", Arrays.asList("Andheri", "Bandra", "Dadar", "Borivali"));
			cityAreaMap.put("Delhi", Arrays.asList("Rohini", "Dwarka", "Saket", "Karol Bagh"));
			cityAreaMap.put("Bengaluru", Arrays.asList("Whitefield", "Koramangala", "Indiranagar"));
			cityAreaMap.put("Hyderabad", Arrays.asList("Gachibowli", "Madhapur", "Begumpet"));
			cityAreaMap.put("Chennai", Arrays.asList("T. Nagar", "Velachery", "Adyar"));
			cityAreaMap.put("Kolkata", Arrays.asList("Salt Lake", "Howrah", "Park Street"));
			cityAreaMap.put("Pune", Arrays.asList("Hinjewadi", "Kothrud", "Baner"));
			cityAreaMap.put("Ahmedabad", Arrays.asList("Navrangpura", "Satellite", "Maninagar"));
			cityAreaMap.put("Jaipur", Arrays.asList("Vaishali Nagar", "Malviya Nagar", "Mansarovar"));
			cityAreaMap.put("Lucknow", Arrays.asList("Alambagh", "Gomti Nagar", "Hazratganj"));

			List<Area> areas = new ArrayList<>();

			for (Map.Entry<String, List<String>> entry : cityAreaMap.entrySet()) {
				String cityName = entry.getKey();
				List<String> areaNames = entry.getValue();

				Optional<City> cityOptional = cityRepository.findByCityName(cityName);
				if (cityOptional.isPresent()) {
					City city = cityOptional.get();
					for (String areaName : areaNames) {
						Area area = new Area();
						area.setAreaName(areaName);
						area.setCity(city);
						areas.add(area);
					}
				}
			}

			areaRepository.saveAll(areas);
		}
	}

	void insertCompany() {
		if (companyRepository.findAll().size() == 0) {
			Company company = new Company();
			company.setCompanyName("BLUE STONE ENERGY PRIVATE LIMITED");
			company.setCompanyAddress("Samartha Aishawarya B-817");
			company.setStateId(14l);
			company.setCityId(47l);
			company.setAreaId(22l);
			company.setContactPerson("Srivats");
			company.setMobileNo(9930873533l);
			company.setEmailId("srivats96@yahoo.com");
			company.setPanNo("AAHCB0444M");
			company.setGstNo("27AAHCB0444M1ZL");
			companyRepository.save(company);

		}

	}

	void insertBranch() {
		if (branchRepository.findAll().size() == 0) {
			Optional<Company> companyOptional = companyRepository.findById(1l);
			if (companyOptional.isPresent()) {
				Company company = companyOptional.get();

				Branch branch1 = new Branch();
				branch1.setBranchName("TamilNadu Bluestone Branch");
				branch1.setBranchAddress("Plot no 68, Mangaikarasi Nagar, Sriukaveripakkam, Velachery, Chennai");
				branch1.setStateId(23L);
				branch1.setCityId(24L);
				branch1.setAreaId(11L);
				branch1.setGstNo("33AAHCB0444M1ZS");
				branch1.setPanNo("AAHCB0444M");
				branch1.setCompany(company);
				Warehouse warehouse1 = new Warehouse();
				warehouse1.setWarehouseName("Chennai Warehouse");
				warehouse1.setWarehouseAddress("TamilNadu");
				warehouse1 = warehouseRepository.save(warehouse1);
				branch1.setWarehouseId(warehouse1.getId());
				branchRepository.save(branch1);

				Branch branch2 = new Branch();
				branch2.setBranchName("Maharashtra Bluestone Branch");
				branch2.setBranchAddress("B/202, Galaxy Complex, Andheri West, Mumbai");
				branch2.setStateId(14L);
				branch2.setCityId(47L);
				branch2.setAreaId(22L);
				branch2.setGstNo("27AACCM9910C1Z9");
				branch2.setPanNo("AACCM9910C");
				branch2.setCompany(company);
				Warehouse warehouse2 = new Warehouse();
				warehouse2.setWarehouseName("Mumbai Warehouse");
				warehouse2.setWarehouseAddress("Andheri, Mumbai");
				warehouse2 = warehouseRepository.save(warehouse2);
				branch2.setWarehouseId(warehouse2.getId());
				branchRepository.save(branch2);

				Branch branch3 = new Branch();
				branch3.setBranchName("Gujarat Bluestone Branch");
				branch3.setBranchAddress("305, Green Plaza, SG Highway, Maninagar, Ahmedabad");
				branch3.setStateId(7L);
				branch3.setCityId(8L);
				branch3.setAreaId(3L);
				branch3.setGstNo("24AABCU9603R1ZM");
				branch3.setPanNo("AABCU9603R");
				branch3.setCompany(company);
				Warehouse warehouse3 = new Warehouse();
				warehouse3.setWarehouseName("Ahmedabad Warehouse");
				warehouse3.setWarehouseAddress("SG Highway, Ahmedabad");
				warehouse3 = warehouseRepository.save(warehouse3);
				branch3.setWarehouseId(warehouse3.getId());
				branchRepository.save(branch3);

				Branch branch4 = new Branch();
				branch4.setBranchName("Karnataka Bluestone Branch");
				branch4.setBranchAddress("12, 3rd Cross, Indiranagar, Koramangala, Bengaluru");
				branch4.setStateId(11L);
				branch4.setCityId(32L);
				branch4.setAreaId(8L);
				branch4.setGstNo("29AAGCG5467K1ZR");
				branch4.setPanNo("AAGCG5467K");
				branch4.setCompany(company);
				Warehouse warehouse4 = new Warehouse();
				warehouse4.setWarehouseName("Bengaluru Warehouse");
				warehouse4.setWarehouseAddress("Indiranagar, Bengaluru");
				warehouse4 = warehouseRepository.save(warehouse4);
				branch4.setWarehouseId(warehouse4.getId());
				branchRepository.save(branch4);

				Branch branch5 = new Branch();
				branch5.setBranchName("Telangana Bluestone Branch");
				branch5.setBranchAddress("8-2-120/76, Road No. 2, Banjara Hills, Hyderabad");
				branch5.setStateId(24L);
				branch5.setCityId(11L);
				branch5.setGstNo("36AAGCT3456L1ZP");
				branch5.setPanNo("AAGCT3456L");
				branch5.setCompany(company);
				Warehouse warehouse5 = new Warehouse();
				warehouse5.setWarehouseName("Hyderabad Warehouse");
				warehouse5.setWarehouseAddress("Banjara Hills, Hyderabad");
				warehouse5 = warehouseRepository.save(warehouse5);
				branch5.setWarehouseId(warehouse5.getId());
				branchRepository.save(branch5);

			}
		}

	}

	void insertUser() {
		if (userRepository.findAll().size() == 0) {
			String password = "1";
			String encodedPassword = encoder.encode(password);

			User user1 = new User();
			user1.setLoginId("superadmin");
			user1.setPassword(encodedPassword);
			user1.setUsername("Super Admin");
			user1.setEmail("superadmin@gmail.com");
			user1.setMobileNo(9999999999L);
			user1.setUserType(UserType.SUPERADMIN);
			userRepository.save(user1);

			User user2 = new User();
			user2.setLoginId("pickupboyTN");
			user2.setPassword(encodedPassword);
			user2.setUsername("Ravi Kumar");
			user2.setEmail("ravi.pickup@gmail.com");
			user2.setMobileNo(9876543210L);
			user2.setUserType(UserType.PICKUPBOY);
			user2.setBranchId(1l);
			userRepository.save(user2);

			User user3 = new User();
			user3.setLoginId("supervisorTN");
			user3.setPassword(encodedPassword);
			user3.setUsername("Amit Sharma");
			user3.setEmail("amit.supervisor@gmail.com");
			user3.setMobileNo(9123456780L);
			user3.setUserType(UserType.MANAGER);
			user3.setBranchId(1l);
			userRepository.save(user3);

			User user4 = new User();
			user4.setLoginId("pickupboyM");
			user4.setPassword(encodedPassword);
			user4.setUsername("Sneha Joshi");
			user4.setEmail("sneha.pickup@gmail.com");
			user4.setMobileNo(9988776655L);
			user4.setUserType(UserType.PICKUPBOY);
			user4.setBranchId(2l);
			userRepository.save(user4);

			User user5 = new User();
			user5.setLoginId("supervisorM");
			user5.setPassword(encodedPassword);
			user5.setUsername("Nitin Desai");
			user5.setEmail("nitin.supervisor@gmail.com");
			user5.setMobileNo(9090909090L);
			user5.setUserType(UserType.MANAGER);
			user5.setBranchId(2l);
			userRepository.save(user5);

		}

	}

	void insertTransporter() {
		if (transporterRepository.findAll().size() == 0) {
			Transporter transporter1 = new Transporter();
			transporter1.setContactPerson("GHANSHYAM SINH");
			transporter1.setTransporterName("SHAKTI TRANSPORT Chennai");
			transporter1.setTransporterAddress("TRANSPORT NAGAR, RAJKOT");
			transporter1.setStateId(23l);
			transporter1.setCityId(24l);
			transporter1.setMobileNo(9904227001L);
			transporter1.setVehicleNo("GJ03AB1234");
			transporter1.setLicenseNo("RJ1234567890");
			transporter1.setGstNo("24AADCB2230M1Z2");
			transporter1.setPanNo("AADCB2230M");
			transporter1.setContractDate("2023-04-01");
			transporter1.setRatePerKg(3.70f);
			transporter1.setCollectedQty(0f);
			transporter1.setBranchId(1L);
			transporterRepository.save(transporter1);

			Transporter transporter2 = new Transporter();
			transporter2.setContactPerson("RAJEEV KHANNA");
			transporter2.setTransporterName("SPEED WHEELS LOGISTICS Chennai");
			transporter2.setTransporterAddress("PLOT 52, SECTOR 7, DWARKA, NEW DELHI");
			transporter2.setStateId(23l);
			transporter2.setCityId(24l);
			transporter2.setAreaId(null);
			transporter2.setMobileNo(9812345678L);
			transporter2.setVehicleNo("DL01CD7890");
			transporter2.setLicenseNo("DL7654321098");
			transporter2.setGstNo("07ABCDE1234F1Z6");
			transporter2.setPanNo("ABCDE1234F");
			transporter2.setContractDate("2023-06-15");
			transporter2.setRatePerKg(4.00f);
			transporter2.setCollectedQty(1500f);
			transporter2.setBranchId(1l);
			transporterRepository.save(transporter2);

			Transporter transporter3 = new Transporter();
			transporter3.setContactPerson("KAVITA NAIR");
			transporter3.setTransporterName("SOUTH LINK CARRIERS Mumbai");
			transporter3.setTransporterAddress("NO. 10, GST ROAD, CHENNAI");
			transporter3.setStateId(14l);
			transporter3.setCityId(47l);
			transporter3.setMobileNo(9845098450L);
			transporter3.setVehicleNo("TN10YZ5678");
			transporter3.setLicenseNo("TN9988776655");
			transporter3.setGstNo("33AAJCB4455K1Z7");
			transporter3.setPanNo("AAJCB4455K");
			transporter3.setContractDate("2023-01-20");
			transporter3.setRatePerKg(3.50f);
			transporter3.setCollectedQty(500f);
			transporter3.setBranchId(2l);
			transporterRepository.save(transporter3);

			Transporter transporter4 = new Transporter();
			transporter4.setContactPerson("IMRAN SHAIKH");
			transporter4.setTransporterName("WESTERN CARGO MOVERS Mumbai");
			transporter4.setTransporterAddress("NASHIK ROAD, MAHARASHTRA");
			transporter4.setStateId(14l);
			transporter4.setCityId(47l);
			transporter4.setMobileNo(9765432187L);
			transporter4.setVehicleNo("MH15MN5432");
			transporter4.setLicenseNo("MH1122334455");
			transporter4.setGstNo("27ABCDX6789L1Z8");
			transporter4.setPanNo("ABCDX6789L");
			transporter4.setContractDate("2024-02-10");
			transporter4.setRatePerKg(3.80f);
			transporter4.setCollectedQty(0f);
			transporter4.setBranchId(2l);
			transporterRepository.save(transporter4);

			Transporter transporter5 = new Transporter();
			transporter5.setContactPerson("NIKHIL PATEL");
			transporter5.setTransporterName("GUJARAT FREIGHTLINES Karnataka");
			transporter5.setTransporterAddress("RING ROAD, SURAT");
			transporter5.setStateId(11l);
			transporter5.setCityId(32l);
			transporter5.setMobileNo(9090909090L);
			transporter5.setVehicleNo("GJ05GH4567");
			transporter5.setLicenseNo("GJ5566778899");
			transporter5.setGstNo("24AABCT9876L1Z3");
			transporter5.setPanNo("AABCT9876L");
			transporter5.setContractDate("2022-12-01");
			transporter5.setRatePerKg(3.65f);
			transporter5.setCollectedQty(200f);
			transporter5.setBranchId(3l);
			transporterRepository.save(transporter5);

		}

	}

}
