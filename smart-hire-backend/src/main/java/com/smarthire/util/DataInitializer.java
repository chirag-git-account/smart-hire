package com.smarthire.util;

import com.smarthire.entity.Company;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import com.smarthire.entity.UserProfile;
import com.smarthire.entity.enums.JobType;
import com.smarthire.entity.enums.Role;
import com.smarthire.entity.enums.UserStatus;
import com.smarthire.repository.CompanyRepository;
import com.smarthire.repository.JobRepository;
import com.smarthire.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, JobRepository jobRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("Checking and seeding demo data for Smart Hire ecosystem...");

        // 1. Admin User
        seedAdminIfNotExists();

        // 2. Job Seeker User
        seedCandidateIfNotExists();

        // 3. Companies & Recruiters with Open Roles
        seedRazorpay();
        seedFlipkart();
        seedGoogle();
        seedMicrosoft();
        seedAmazon();
        seedSwiggy();
        seedZomato();
        seedCred();
        seedUber();
        seedPhonePe();
        seedZerodha();

        // 4. Seed Company Profiles
        seedCompanyProfiles();

        log.info("Smart Hire ecosystem seeding complete. Total companies active: {}", companyRepository.count());
    }

    private void seedAdminIfNotExists() {
        if (userRepository.findByEmail("admin@smarthire.ai").isEmpty()) {
            User admin = User.builder()
                    .fullName("System Administrator")
                    .email("admin@smarthire.ai")
                    .password(passwordEncoder.encode("password123"))
                    .phone("+91-9999900000")
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(admin);
            log.info("Seeded Admin user.");
        }
    }

    private void seedCandidateIfNotExists() {
        if (userRepository.findByEmail("candidate@smarthire.ai").isEmpty()) {
            User candidate = User.builder()
                    .fullName("Rahul Sharma")
                    .email("candidate@smarthire.ai")
                    .password(passwordEncoder.encode("password123"))
                    .phone("+91-9876543210")
                    .role(Role.JOB_SEEKER)
                    .status(UserStatus.ACTIVE)
                    .build();

            UserProfile candidateProfile = UserProfile.builder()
                    .user(candidate)
                    .headline("Senior Java Backend Engineer | Spring Boot, Microservices & Cloud")
                    .summary("Passionate software engineer with 4.5+ years building scalable distributed microservices in Java, Spring Boot, MySQL, and Docker.")
                    .currentLocation("Bengaluru, India")
                    .totalExperienceYears(4.5)
                    .expectedSalary("₹22,00,000")
                    .noticePeriodDays(30)
                    .skills(Arrays.asList("Java", "Spring Boot", "MySQL", "REST APIs", "Docker", "Microservices", "Hibernate", "Git", "React"))
                    .build();
            candidate.setProfile(candidateProfile);
            userRepository.save(candidate);
            log.info("Seeded Candidate profile.");
        }
    }

    private User getOrCreateRecruiter(String fullName, String email, String phone, String company, String designation) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User recruiter = User.builder()
                    .fullName(fullName)
                    .email(email)
                    .password(passwordEncoder.encode("password123"))
                    .phone(phone)
                    .role(Role.RECRUITER)
                    .status(UserStatus.ACTIVE)
                    .companyName(company)
                    .designation(designation)
                    .build();
            return userRepository.save(recruiter);
        });
    }

    private void seedCompanyIfNotExists(String name, String industry, String location, String website, String size, int founded, String desc, User owner) {
        if (companyRepository.findByNameIgnoreCase(name).isEmpty()) {
            Company c = Company.builder()
                    .name(name)
                    .industry(industry)
                    .location(location)
                    .website(website)
                    .employeeSize(size)
                    .foundedYear(founded)
                    .description(desc)
                    .owner(owner)
                    .build();
            companyRepository.save(c);
        }
    }

    private void seedCompanyProfiles() {
        seedCompanyIfNotExists("Google", "Search, Cloud & AI", "Bengaluru & Hyderabad, India", "https://google.com", "150,000+", 1998, "World leader in search, AI models, Android, and hyper-scale cloud computing.", userRepository.findByEmail("recruiter@google.com").orElse(null));
        seedCompanyIfNotExists("Microsoft", "Cloud Computing & Enterprise Software", "Bengaluru & Hyderabad, India", "https://microsoft.com", "220,000+", 1975, "Empowering every person and organization on the planet to achieve more with Azure and Office.", userRepository.findByEmail("recruiter@microsoft.com").orElse(null));
        seedCompanyIfNotExists("Amazon", "Cloud (AWS) & Global E-Commerce", "Bengaluru, India", "https://amazon.com", "1,500,000+", 1994, "Earth's most customer-centric company, pioneering AWS cloud infrastructure and logistics.", userRepository.findByEmail("recruiter@amazon.com").orElse(null));
        seedCompanyIfNotExists("Razorpay", "FinTech & Payment Infrastructure", "Bengaluru, India", "https://razorpay.com", "3,500+", 2014, "India's leading full-stack payments solution enabling automated billing and neo-banking.", userRepository.findByEmail("recruiter@razorpay.com").orElse(null));
        seedCompanyIfNotExists("Flipkart", "E-Commerce & Supply Chain Logistics", "Bengaluru, India", "https://flipkart.com", "30,000+", 2007, "India's homegrown e-commerce pioneer powering digital commerce for millions of consumers.", userRepository.findByEmail("recruiter@flipkart.com").orElse(null));
        seedCompanyIfNotExists("Swiggy", "FoodTech & Hyperlocal Quick Commerce", "Bengaluru, India", "https://swiggy.com", "10,000+", 2014, "India's leading on-demand delivery platform offering food delivery and Instamart quick commerce.", userRepository.findByEmail("recruiter@swiggy.com").orElse(null));
        seedCompanyIfNotExists("Zomato", "Food Delivery & Restaurant Discovery", "Gurugram, India", "https://zomato.com", "5,000+", 2008, "Connecting food lovers with exceptional dining, grocery delivery with Blinkit, and Hyperpure supply.", userRepository.findByEmail("recruiter@zomato.com").orElse(null));
        seedCompanyIfNotExists("CRED", "FinTech & High-Trust Community", "Bengaluru, India", "https://cred.club", "1,200+", 2018, "Members-only platform rewarding creditworthy individuals with exclusive lifestyle and financial privileges.", userRepository.findByEmail("recruiter@cred.club").orElse(null));
        seedCompanyIfNotExists("Uber", "Mobility & Real-Time Logistics", "Bengaluru & Hyderabad, India", "https://uber.com", "32,000+", 2009, "Igniting opportunity by setting the world in motion with high-throughput dispatch engineering.", userRepository.findByEmail("recruiter@uber.com").orElse(null));
        seedCompanyIfNotExists("PhonePe", "Digital Payments & UPI Rail Platform", "Bengaluru, India", "https://phonepe.com", "8,000+", 2015, "India's #1 digital payments app processing over 150 Million daily transactions with ACID guarantees.", userRepository.findByEmail("recruiter@phonepe.com").orElse(null));
        seedCompanyIfNotExists("Zerodha", "FinTech & Retail Brokerage", "Bengaluru, India", "https://zerodha.com", "1,100+", 2010, "India's biggest stock brokerage platform (Kite) engineered with low-latency systems.", userRepository.findByEmail("recruiter@zerodha.com").orElse(null));
    }

    private void seedRazorpay() {
        User recruiter = getOrCreateRecruiter("Priya Sharma", "recruiter@razorpay.com", "+91-9876543211", "Razorpay", "Lead Tech Talent Partner");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job1 = Job.builder()
                    .title("Senior Java Backend Engineer")
                    .description("Design high-throughput payment processing APIs, implement event-driven architectures, and optimize MySQL transaction engines.")
                    .companyName("Razorpay")
                    .location("Bengaluru (Hybrid)")
                    .jobType(JobType.HYBRID)
                    .salaryMin(1800000L)
                    .salaryMax(3200000L)
                    .experienceMinYears(3)
                    .educationRequirement("B.Tech / B.E. / M.Tech in Computer Science")
                    .responsibilities("• Architect resilient RESTful APIs in Spring Boot\n• Design database schemas and optimize indexing\n• Build asynchronous pipelines with Kafka")
                    .benefits("• Comprehensive Health Insurance\n• Annual Performance Bonus\n• Wellness & Home Office Allowance")
                    .deadline("2026-10-30")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "REST APIs", "Docker", "Microservices"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();

            Job job2 = Job.builder()
                    .title("Lead Cloud & Distributed Systems Architect")
                    .description("Spearhead cloud-native infrastructure scaling, zero-downtime deployments, and distributed database sharding at Razorpay scale.")
                    .companyName("Razorpay")
                    .location("Remote (India)")
                    .jobType(JobType.REMOTE)
                    .salaryMin(3500000L)
                    .salaryMax(5500000L)
                    .experienceMinYears(6)
                    .educationRequirement("B.Tech / M.Tech in CS or equivalent")
                    .responsibilities("• Drive architectural decisions for high-volume transactions\n• Enforce security and compliance standards\n• Mentor senior engineering teams")
                    .benefits("• Top of market ESOPs\n• Unlimited PTO\n• Global conference sponsorships")
                    .deadline("2026-12-01")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "Kubernetes", "AWS", "Kafka", "Microservices"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();

            jobRepository.saveAll(Arrays.asList(job1, job2));
        }
    }

    private void seedFlipkart() {
        User recruiter = getOrCreateRecruiter("Amit Verma", "recruiter@flipkart.com", "+91-9876543212", "Flipkart", "Senior Engineering Recruiter");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Full Stack React & Spring Boot Developer")
                    .description("Join Flipkart's Marketplace Core team to build blazing fast merchant dashboards using React 18, Vite, Spring Boot 3, and MySQL.")
                    .companyName("Flipkart")
                    .location("Bengaluru (On-site)")
                    .jobType(JobType.FULL_TIME)
                    .salaryMin(1400000L)
                    .salaryMax(2600000L)
                    .experienceMinYears(2)
                    .educationRequirement("B.Tech / MCA / B.Sc Computer Science")
                    .responsibilities("• Develop reusable React UI components\n• Integrate secure JWT-authenticated REST APIs\n• Collaborate with Product & UX teams")
                    .benefits("• Flexible work hours\n• Free transport & meals\n• Learning reimbursement")
                    .deadline("2026-11-15")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "React", "JavaScript", "MySQL", "CSS"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }

    private void seedGoogle() {
        User recruiter = getOrCreateRecruiter("Neha Kapoor", "recruiter@google.com", "+91-9876543213", "Google", "Staff Talent Partner - Core AI");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job1 = Job.builder()
                    .title("Staff Software Engineer - AI / ML Platform")
                    .description("Lead the development of large-scale distributed machine learning training and inference infrastructure powering next-generation Google Workspace AI capabilities.")
                    .companyName("Google")
                    .location("Bengaluru (Hybrid)")
                    .jobType(JobType.HYBRID)
                    .salaryMin(4500000L)
                    .salaryMax(7500000L)
                    .experienceMinYears(6)
                    .educationRequirement("B.Tech / M.Tech / Ph.D. in Computer Science")
                    .responsibilities("• Architect high-throughput ML pipelines\n• Optimize GPU/TPU cluster workloads\n• Drive technical strategy across multi-functional AI research teams")
                    .benefits("• World-class stock grant (GSUs)\n• Comprehensive wellness & fertility benefits\n• 401(k) / Provident fund matching")
                    .deadline("2026-12-31")
                    .requiredSkills(Arrays.asList("Java", "Python", "Kubernetes", "Docker", "Distributed Systems", "Microservices", "GCP"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();

            Job job2 = Job.builder()
                    .title("Senior Cloud Infrastructure & Site Reliability Engineer")
                    .description("Scale Google Cloud Platform foundation services with 99.999% availability, zero-trust security policies, and automated fault recovery.")
                    .companyName("Google")
                    .location("Hyderabad (Hybrid)")
                    .jobType(JobType.HYBRID)
                    .salaryMin(3200000L)
                    .salaryMax(5500000L)
                    .experienceMinYears(4)
                    .educationRequirement("B.Tech in Computer Science / IT")
                    .responsibilities("• Build automated deployment orchestration pipelines\n• Manage multi-region Kubernetes clusters\n• Participate in high-severity incident resolution")
                    .benefits("• Free gourmet dining & micro-kitchens\n• Hybrid flexibility\n• Sabbatical policy")
                    .deadline("2026-11-30")
                    .requiredSkills(Arrays.asList("Java", "Go", "Kubernetes", "Terraform", "Docker", "Linux", "GCP"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();

            jobRepository.saveAll(Arrays.asList(job1, job2));
        }
    }

    private void seedMicrosoft() {
        User recruiter = getOrCreateRecruiter("Rohan Mehra", "recruiter@microsoft.com", "+91-9876543214", "Microsoft", "Principal Talent Partner - Azure");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job1 = Job.builder()
                    .title("Principal Backend Engineer - Azure Cloud Core")
                    .description("Architect resilient distributed storage backends, hyper-scale virtual networking services, and multi-tenant compute engines on Azure.")
                    .companyName("Microsoft")
                    .location("Bengaluru (Hybrid)")
                    .jobType(JobType.HYBRID)
                    .salaryMin(3800000L)
                    .salaryMax(6000000L)
                    .experienceMinYears(5)
                    .educationRequirement("B.Tech / M.Tech in Computer Science")
                    .responsibilities("• Lead architectural design for mission-critical cloud tiers\n• Optimize memory footprint and CPU utilization in high-load services\n• Mentor senior engineers")
                    .benefits("• Microsoft stock awards (RSUs)\n• Annual bonus & health coverage\n• Generous parental leave")
                    .deadline("2026-12-15")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "C#", "Microservices", "Docker", "Azure", "SQL"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();

            Job job2 = Job.builder()
                    .title("Full Stack Cloud Security Engineer")
                    .description("Build threat detection dashboards, automated vulnerability scanning platforms, and IAM policy enforcement tools for enterprise customers.")
                    .companyName("Microsoft")
                    .location("Remote (India)")
                    .jobType(JobType.REMOTE)
                    .salaryMin(2500000L)
                    .salaryMax(4200000L)
                    .experienceMinYears(3)
                    .educationRequirement("B.Tech / MCA in Computer Science")
                    .responsibilities("• Develop reactive security monitoring frontends with React\n• Integrate secure OAuth2 & JWT backend authorization services\n• Conduct threat modeling")
                    .benefits("• 100% Remote work stipend\n• Fitness reimbursement\n• Microsoft hardware allowance")
                    .deadline("2026-11-20")
                    .requiredSkills(Arrays.asList("React", "JavaScript", "Java", "Spring Boot", "REST APIs", "Docker", "OAuth2"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();

            jobRepository.saveAll(Arrays.asList(job1, job2));
        }
    }

    private void seedAmazon() {
        User recruiter = getOrCreateRecruiter("Ananya Sen", "recruiter@amazon.com", "+91-9876543215", "Amazon", "Lead AWS Tech Talent Acquisition");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Software Development Engineer II (SDE-2) - AWS Core")
                    .description("Design high-throughput distributed database microservices, asynchronous transaction brokers, and customer-facing management APIs for AWS Commerce platform.")
                    .companyName("Amazon")
                    .location("Bengaluru (On-site)")
                    .jobType(JobType.FULL_TIME)
                    .salaryMin(2800000L)
                    .salaryMax(4800000L)
                    .experienceMinYears(3)
                    .educationRequirement("Bachelor's or Master's in Computer Science")
                    .responsibilities("• Build distributed microservices in Java handling millions of transactions per minute\n• Optimize data storage in DynamoDB and MySQL\n• Own full operational lifecycle")
                    .benefits("• Relocation assistance\n• Amazon RSU equity\n• Comprehensive health insurance")
                    .deadline("2026-11-30")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "AWS", "DynamoDB", "MySQL", "Kafka", "Microservices"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }

    private void seedSwiggy() {
        User recruiter = getOrCreateRecruiter("Vikram Das", "recruiter@swiggy.com", "+91-9876543216", "Swiggy", "Head of Engineering Talent");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Lead Logistics & Real-Time Geospatial Engineer")
                    .description("Power India's largest hyper-local delivery fleet. Build routing algorithms, real-time tracking streams, and order dispatch microservices handling 50k+ req/sec.")
                    .companyName("Swiggy")
                    .location("Bengaluru (Hybrid)")
                    .jobType(JobType.HYBRID)
                    .salaryMin(2800000L)
                    .salaryMax(4600000L)
                    .experienceMinYears(4)
                    .educationRequirement("B.Tech / B.E. in Computer Science")
                    .responsibilities("• Design low-latency geo-clustering microservices in Java\n• Manage streaming pipelines using Apache Kafka\n• Optimize caching strategies with Redis")
                    .benefits("• Swiggy One & food allowances\n• High-growth ESOP grants\n• Flexible hybrid workplace")
                    .deadline("2026-10-31")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "Kafka", "Redis", "MySQL", "Microservices", "Docker"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }

    private void seedZomato() {
        User recruiter = getOrCreateRecruiter("Sneha Nair", "recruiter@zomato.com", "+91-9876543217", "Zomato", "Lead Technical Recruiter");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Senior Frontend & Web Performance Architect")
                    .description("Craft fluid, responsive, micro-frontend web experiences for millions of foodies and restaurant partners across India and UAE using React, TypeScript, and modern CSS.")
                    .companyName("Zomato")
                    .location("Gurugram (On-site)")
                    .jobType(JobType.FULL_TIME)
                    .salaryMin(2200000L)
                    .salaryMax(3800000L)
                    .experienceMinYears(3)
                    .educationRequirement("B.Tech / BCA / MCA in Computer Science")
                    .responsibilities("• Build performant React components with sub-100ms load times\n• Implement clean client-side state management\n• Collaborate with UX design teams on design system evolution")
                    .benefits("• Zomato Pro Gold privileges\n• ESOP wealth creation programs\n• Premium medical coverage")
                    .deadline("2026-11-25")
                    .requiredSkills(Arrays.asList("React", "JavaScript", "CSS", "HTML", "REST APIs", "TypeScript", "Vite"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }

    private void seedCred() {
        User recruiter = getOrCreateRecruiter("Kunal Roy", "recruiter@cred.club", "+91-9876543218", "CRED", "Lead Talent Partner");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Senior Backend Architect - High-Throughput Payments")
                    .description("Build ultra-reliable payment processing rails, double-entry ledger bookkeeping engines, and instant credit settlement systems for India's most creditworthy community.")
                    .companyName("CRED")
                    .location("Bengaluru (Hybrid)")
                    .jobType(JobType.HYBRID)
                    .salaryMin(3500000L)
                    .salaryMax(5800000L)
                    .experienceMinYears(4)
                    .educationRequirement("B.Tech / M.Tech in Computer Science")
                    .responsibilities("• Architect zero-data-loss payment settlement engines\n• Optimize MySQL and PostgreSQL transaction isolation levels\n• Maintain 99.999% uptime for core financial services")
                    .benefits("• Top of market ESOPs\n• Unlimited leave policy\n• Daily chef-prepared meals & club perks")
                    .deadline("2026-12-15")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "Kafka", "Microservices", "REST APIs", "Docker"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }

    private void seedUber() {
        User recruiter = getOrCreateRecruiter("Tanvi Joshi", "recruiter@uber.com", "+91-9876543219", "Uber", "Staff Technical Recruiter");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Senior Distributed Systems Engineer - Dispatch Core")
                    .description("Engineer the real-time matching and dispatch algorithms that connect millions of riders and drivers across 70+ countries every minute.")
                    .companyName("Uber")
                    .location("Bengaluru (Hybrid)")
                    .jobType(JobType.HYBRID)
                    .salaryMin(3800000L)
                    .salaryMax(6200000L)
                    .experienceMinYears(5)
                    .educationRequirement("B.Tech / M.Tech in Computer Science")
                    .responsibilities("• Build distributed real-time state machines in Java and Go\n• Scale Kafka messaging clusters for global event streaming\n• Implement chaos engineering & automated failover")
                    .benefits("• Uber ride credits & Uber Eats perks\n• US equity stock grants (RSUs)\n• Comprehensive family healthcare")
                    .deadline("2026-12-20")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "Kafka", "Docker", "Kubernetes", "Microservices", "MySQL"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }

    private void seedPhonePe() {
        User recruiter = getOrCreateRecruiter("Rajesh Kulkarni", "recruiter@phonepe.com", "+91-9876543220", "PhonePe", "Director of Talent Acquisition");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Staff Database & Distributed Storage Architect")
                    .description("Architect India's largest UPI transactional backbone handling 150+ Million daily transactions with strict ACID compliance, sharding, and sub-second latencies.")
                    .companyName("PhonePe")
                    .location("Bengaluru (On-site)")
                    .jobType(JobType.FULL_TIME)
                    .salaryMin(3400000L)
                    .salaryMax(5400000L)
                    .experienceMinYears(5)
                    .educationRequirement("B.Tech / B.E. in Computer Science")
                    .responsibilities("• Design multi-master MySQL sharding and caching topologies\n• Lead high-concurrency database tuning and failover orchestration\n• Partner with product teams on payment rail scaling")
                    .benefits("• Generous stock options\n• Annual executive health checkups\n• High performance incentive bonuses")
                    .deadline("2026-11-30")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "Hibernate", "Microservices", "REST APIs", "Docker"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }

    private void seedZerodha() {
        User recruiter = getOrCreateRecruiter("Deepak Verma", "recruiter@zerodha.com", "+91-9876543221", "Zerodha", "Lead Systems Recruiter");
        if (jobRepository.findByRecruiter(recruiter).isEmpty()) {
            Job job = Job.builder()
                    .title("Systems & Low-Latency Trading Platform Engineer")
                    .description("Join India's largest retail stock brokerage (Kite platform). Build lightning-fast order matching pipes, WebSocket market data feeds, and robust risk management engines.")
                    .companyName("Zerodha")
                    .location("Bengaluru (Remote Friendly)")
                    .jobType(JobType.REMOTE)
                    .salaryMin(2600000L)
                    .salaryMax(4600000L)
                    .experienceMinYears(3)
                    .educationRequirement("B.Tech in Computer Science or self-taught engineering mastery")
                    .responsibilities("• Build low-latency trading microservices with minimal dependencies\n• Implement high-throughput WebSocket protocols for live tickers\n• Keep codebase simple, maintainable, and highly optimized")
                    .benefits("• Zero bureaucracy culture\n• Annual profit-sharing bonus\n• Freedom of technology choice")
                    .deadline("2026-12-10")
                    .requiredSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "REST APIs", "Docker", "JavaScript", "Microservices"))
                    .recruiter(recruiter)
                    .isActive(true)
                    .build();
            jobRepository.save(job);
        }
    }
}
