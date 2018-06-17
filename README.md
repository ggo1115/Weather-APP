# Weather-APP

본 소프트웨어는 
1) 현재 위치한 지역의 날씨 및 선택한 지역의 날씨를 확인하는 기능
2) 어제와 오늘의 기온(3시간 단위)을 그래프로 비교하여 나타내는 기능
3) 계절에 맞는 음식을 추천해주는 기능
4) 원하는 지역 즐겨찾기 추가 및 삭제하는 기능
5) 원하는 두 지역의 현재, 오늘, 내일 날씨를 비교해주는 기능
6) 화씨 및 섭씨 단위 선택하는 기능
7) 원하는 시간에 원하는 알림(미세먼지, 비/눈, 일교차)를 원하는 방식(소리, 진동, 무음)으로 푸쉬알림 해주는 기능
8) 현재위치정보 제공 여부 선택 기능
을 지원한다.

세부적인 구성은 다음과 같다.

1. Air_Data_PM10.java : PM10(미세먼지 농도) 의 지역별 정보를 담는 class 소스코드
2. Air_Data_PM25.java : PM25(초미세먼지 농도)의 지역별 정보를 담는 class 소스코드
3. AreaComp.java : 지역비교화면에서 작동하는 소스코드(선택된 두 지역의 날씨를 띄어줌)
4. BookMarkAdapter.java : 즐겨찾기 list를 위한 adapter 소스코드 ( 사용 X )
5. GPSActivity.java : 현재위치를 사용한 날씨정보 제공하기 위해서 사용되는 소스코드(FINE_LOCATION 권한 허용여부를 판단하고, 
                      거부된 경우에는 허용할 수 있도록 조치를 하고, 허용된 경우엔 GPS를 통해 위도 경도를 획득하고 Geocoder 클래스를 해
                      주소를 획득한다. GPS가 꺼진 경우에는 gps를 켜도록 조치를 한다.)
6. GlideModule.java : 이미지 로딩 라이브러리(Glide) 에서 사용되는 api 사용하기 위한 소스코드
7. Location_Data.java : 위치정보를 담는 class소스코드(위도, 경도, 주소, 지역코드)
8. MainActivity.java : 첫화면에서 작동하는 소스코드(현재위치/선택한 지역의 날씨 정보 / 날씨 그래프 / 계절에 맞는 음식을 추천하기 위해 firebase storage에 
                      저장된 음식 사진을 로딩하여 띄워준다. 앱이 실행될 때마다 내부DB에서 사용자가 설정한 정보를 가져온다. 앱이 종료될 때마다 내부DB에 
                      사용자가 설정한 정보를 저장함. 새로고침 버튼을 누를때마다 화면이 새로고침 되게 함. 또한 firebase-Test에 내일의 날씨가 저장되어있지 
                      않은 경우 파싱된 내일의 날씨를 firebase에 저장함)
9. ReadNWriteinDBforTemp.java : firebase에서 지역별 지역코드를 읽어오고, 지역별 내일 날씨를 저장하는 소스코드
10. ReceiveAirPM10.java : PM10(미세먼지 농도)를 한국환경공단_대기오염정보 조회 서비스 API에서 파싱하여 Air_Data_PM10 클래스에 저장하는 소스코드
11. ReceiveAirPM25.java : PM25(초미세먼지 농도)를 한국환경공단_대기오염정보 조회 서비스 API에 파싱하여 Air_Data_PM25 클래스에 저장하는 소스코드
12. ReceiveWeather.java : 주어진 지역코드를 통해 기상청 rss에서 해당 지역의 날씨를 파싱하여 Weatherinfo_Data 클래스로 이루어진 ArrayList에 저장하는 소스코드
13. SearchAdapter.java : 리스트뷰에 연결할 어답터의 기본 기능 작업
14. Select_ForComp.java : 비교를 위한 두 지역을 선택하기 위한 소스코드(지역 2개를 모두 선택한 경우에만 지역비교한 날씨정보화면으로 넘어가도록 함)
15. Selection_Location_Air.java : 파싱된 미세먼지 정보 중 선택한 지역 혹은 위치하고 있는 지역에 대한 미세정보 불러오기 위한 소스코드
16. SettingActivity.java : 설정화면에서 작동하는 소스코드 ( 화씨/섭씨 설정화면, 현재위치정보제공여부 설정화면, 푸쉬알림 설정화면 으로 이동하게 함)
17. SettingAlarm.java : 푸쉬알람을 설정하는 화면에서 작동하는 소스코드 ( 푸쉬알람을 사용하는 경우 알람 방법(소리/진동/무음), 알람 정보(눈/비, 미세먼지, 일교차), 
                        알람시각)을 설정할 수 있음)
18. SettingArea.java : 지역 리스트 입력, 지역 검색 기능 코드(리스트 생성, 어답터 연걸, 검색 수행)
19. SettingAreaMenu.java : 지역추가화면(지역 즐겨찾기 목록)화면에서 작동하는 코드 ( 즐겨찾기 추가한 지역을 추가 및 원하는 지역 삭제 및 지역을 선택하면 첫화면으로
                            이동하여 선택한 지역의 정보 띄움)
20. SettingCurLocation.java : 현재위치정보제공여부 설정 화면에서 작동하는 소스 코드
21. SettingTempScale.java : 화씨/섭씨 단위 설정 화면에서 작동하는 소스 코드
22. Temp_Data_fo_Day.java : firebase에 저장하기 위한 기온정보(3시, 6시, 9시, 12시, 15시, 18시, 21시, 24시)를 담는 class 소스코드
23. Weatherinfo_Data.java : 날씨정보(날짜(오늘/내일/모레), 시간단위, 현재온도, 최고온도, 최저온도, 하늘상태, 날씨상태, 강수확률, 풍속, 풍향, 습도) 를 담는 class 소스코드
24. activity_alarm_setting.xml : 푸쉬알람 설정 화면 layout 소스코드
25. activity_area_c.xml : 날씨비교화면 layout 소스코드
26. activity_area_menu.xml : 지역선택(즐겨찾기목록)화면 layout 소스코드
27. activity_area_s.xml : 지역추가화면(지역선택화면에서 + 버튼 눌렀을 때 이동하는 화면) layout 소스코드
28. activity_currentlocation.xml : 현재위치정보제공설정화면 layout 소스코드
29. activity_main.xml : 첫화면(현재위치 혹은 선택한 지역의 날씨정보 및 기온 비교 그래프 및 추천 음식 화면과 메뉴) layout 소스코드
30. activity_select_comp.xml : 비교를 위한 두 지역 선택 화면 layout 소스코드
31. activity_setting.xml : 설정화면 layout 소스코드
32. activity_temp.xml : 화씨/섭씨 단위 설정 화면 layout 
33. row_listview.xml : 리스트뷰의 셀에 데이터를 노출
34. Dual-Y.html : 두 지역 날씨 비교를 위한 Google Chart API(WebView)
