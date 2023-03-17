# 코비드맵 🦠
## 🤔 프로젝트 설명

> 코로나 선별 진료소를 한눈에 볼 수 있는 앱입니다! (프로젝트 내부 app/release/app-release.apk를 설치를 통해 테스트가 가능합니다.)

<br>

### 💻 기술스택 
#### ▪️ Client
<p>
 <img src="https://img.shields.io/badge/Anroid-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
 <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">
 <img src="https://img.shields.io/badge/Retrofit2-3E4348?style=for-the-badge&logo=Square&logoColor=white">
 <img src="https://img.shields.io/badge/OkHttp-3E4348?style=for-the-badge&logo=Square&logoColor=white">
 <img src="https://img.shields.io/badge/NaverMap-03C75A?style=for-the-badge&logo=Naver&logoColor=white">
 <img src="https://img.shields.io/badge/Room-003B57?style=for-the-badge&logo=Sqlite&logoColor=white">
 <img src="https://img.shields.io/badge/MVVM-3DDC84?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/Coroutine-3DDC84?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/DataBinding-0F9D58?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/Hilt-0F9D58?style=for-the-badge&logo=&logoColor=white">
 <img src="https://img.shields.io/badge/TedPermission-0F9D58?style=for-the-badge&logo=&logoColor=white">
</p>

#### ▪️ Server
<p>
 <img src="https://img.shields.io/badge/OpenAPI-40AEF0?style=for-the-badge&logo=&logoColor=white">
</p>

<br>

### 🛠 구현 사항
##### 1️⃣ Splash 화면
###### Rest 통신을 통해 받아온 선별 진료소 목록 100개를 Room을 통해 저장하여 MainActivity에서 사용할 수 있도록 구현 하였습니다.

##### 2️⃣ 코로나 선별진료소 지도
###### NaverMap을 통해 MapView를 구성하고 Room에 저장된 선별 진료소 목록을 불러와 지도에 마커로 표시하도록 구현 하였습니다.

##### 3️⃣ 마커 정보창
###### 마커를 클릭시에는 해당 진료소로 지도를 이동 후 진료소의 정보를 표시하도록 구현 하였습니다.

##### 4️⃣ 현재위치 
###### 앱이 시작되면 현재위치를 바로 표시하며 버튼을 통해 현재위치를 받아 오는것 또한 가능합니다. 아울러, 해당 위치에 마커를 찍도록 구현 하였습니다.

<br>

### 🎥 시연 화면
<div align="center">
 <img width="30%" alt="app_main" src="https://user-images.githubusercontent.com/65700842/225917789-2a3a4da5-5b72-4caf-9d59-5790db77c1cc.gif">
</div>

<br>

### 😎 프로젝트 사용기술 설명
##### 1️⃣ Dagger Hilt를 활용하여 의존성을 주입 해주었습니다.
##### 2️⃣ MVVM 디자인 기반으로 프로젝트를 진행 하였습니다.
##### 3️⃣ Coroutine , Coroutine Flow를 통한 비동기 처리를 했습니다.
##### 4️⃣ Retrofit2를 통해 Rest통신을 하였습니다.
##### 5️⃣ Repository를 사용하여 Data를 관리 하였습니다.
##### 6️⃣ TedPermission 라이브러리를 통한 권한 체크를 했습니다.
##### 7️⃣ Room을 통해 미리 선별진료소의 목록을 내부 저장소에 저장하였습니다.
##### 8️⃣ NaverMap을 활용하여 Map화면을 구현 하였습니다.
