# 휴가 신청 시스템
* 사용 기술
    * Spring boot, Spring Security, JPA, Java, Javascript, MySQL, H2 Database

* 구현 기능
    * 회원가입(AccountController)
        * 웹 화면에서 회원 가입에 필요한 input 값들을 SignUpForm 형태로 받아와 사용자 모델인 Account 클래스와 같이 생성되도록 구현
        * password의 경우 input 값을 그대로 저장하지 않고 암호화 처리를 하기 위해 BCryptPasswordEncoder를 사용하여 DB저장시 암호화 처리
        * 또한 SignUpFormValidator를 통해 이미 존재하는 아이디의 경우 error를 발생
                   
    * 로그인/로그아웃(AccountController)
        * Spring Security를 사용하여 SecurityConfig 설정 후 웹 화면에서 LoginForm 형태로 input 값을 받아와 Controller에서 처리하도록 구현
        * SecurityConfig 설정을 통해 로그아웃 요청시 홈화면으로 이동 설정
        * @CurrentUser 어노테이션을 구현하여 런타임시간동안 해당 유저에 대한 정보를 Controller의 파라미터로 제공하여 로그인 여부 판별 가능
    
    * 휴가 신청(VacationController)
        * 휴가 신청에 필요한 VacationForm과 Account 정보를 POST 요청을 통해 VacationService의 saveVacation 메소드와 AccountService의 saveAccount 메소드 실행
        * 휴가 신청 상태와 휴가 종류는 Enum으로 관리하며 추후 변경시 Enum 데이터 변경을 통해 변경이 용이하도록 구현
        * 개인별 휴가 신청 내역의 경우 Account 클래스와 Vacation 클래스의 일대다 관계로 지정되어있는 리스트형태의 vacations 사용 (사용자 한명당 여러건의 휴가 신청건 수를 갖고 있을 수 있기 때문)
                
    * 휴가 취소(VacationController)
        * @PostMapping(value = "/detail-vacation/{vacationId}/cancel")과 같은 형식을 통해 취소를 원하는 휴가 요청건의 Id 값을 파라미터로 사용
        * VacationService의 cancelVacation 메소드와 AccountService의 saveAccount 메소드 실행
        * Account와 Vacation의 저장 및 수정이 요청될때 EntityManager의 persist와 merge를 사용하여 DB에 저장 및 업데이트가 가능하도록 구현
        
    * DB 스케쥴링
        * Spring 에서 제공하는 Scheule 기능을 사용하여 구현했습니다.
        * Scheduler 클래스를 생성하여 @Scheduled(cron = "0 0 0 1 1 ?")과 같이 cron 설정을 통해 매년 1월 1일가 되는 자정에 
        DB에 저장되어 있는 모든 사용자들의 연차 갯수를 15일로 변경하도록 구현
        
    * property 설정
        * application.yml을 사용하여 어플리케이션 실행시 InMemory DB 기능을 제공하는 H2 DB를 사용, 개발 및 테스트 환경에서 사용 
        * application-dev.yml 사용시 Mysql DB를 사용하여 데이터를 영구히 저장할 수 있도록 설정
        * JPA를 사용했기 때문에 해당 파일의 datasource 부분을 변경하면 다른 DBMS로 전환 가능