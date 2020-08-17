# 使用 Noted! 一鍵筆記網路資源

- **Noted!** 是一套簡便的筆記管理工具。
- 看到任何你感興趣的 **文章** 、 **影片** 或 **地標** ，只要點擊分享，就可以將它存入 **Noted!** 以便查看與整理。
- 再也不用害怕找不到看過一眼卻散落四處的網路資源。 <br><br>
<a href="https://play.google.com/store/apps/details?id=com.connie.noted"><img src="https://camo.githubusercontent.com/9b43e9e7bdf73be90eaee8bf94cf61440638567e/68747470733a2f2f692e696d6775722e636f6d2f49353862574c642e706e67" width="150"></a>

<br><br>

## 如何開始？

<h3 id="part1"> 1.  從任一網路資源將筆記匯入 Noted! </h3>

<center>
<img src="https://github.com/tzuxlin/Week-4/blob/master/addNote.gif" height=370 href="#"/>
</center>

* 點擊網路資源的 **分享** 按鈕，選擇分享到 **Noted!** ，就會將內容存為筆記。
* 運用 **Jsoup Library** 進行資料解析，從 HTML DOM 結構中擷取標題、圖片、內文等資訊。

<br>

#### **筆記內容**

<img src="https://i.imgur.com/jtHvtAK.png" height=350 boarder=1/> <img src="https://i.imgur.com/OP6kAXO.png" height=350 boarder=1/> <img src="https://i.imgur.com/RVdct0i.png" height=350 boarder=1/>

* 擷取文章、影片、地標等網路資源，點擊下方 **查看完整內容** 按鈕，可以連結至原始頁面。
* 整合 **YouTube Player API** ，使用者可以直接在頁面中觀看 YouTube 匯入的影片。
* 透過 **Google Maps SDK** ，使用者能夠在頁面中直接進行 Google Maps 的基本瀏覽操作。

#### **筆記列表**

<img src="https://github.com/tzuxlin/Week-4/blob/master/Note_gif_note.gif" height=350/> <img src="https://github.com/tzuxlin/Week-4/blob/master/note_gif_filter.gif" height=350/>

* 以 RecyclerView 顯示筆記列表，可以選擇 **條列式佈局（List）** 或是由 `StaggeredGridLayoutManager` 實作的 **瀑布式佈局（Waterfall）**。
* 點擊 Navigation Drawer 中的選單項目，進一步篩選畫面顯示的筆記列表。

<br>

<h3 id="part2">  2. 下一步，將筆記整理成主題性分類板 </h3>

<img src="https://github.com/tzuxlin/Week-4/blob/master/add2board.gif" height=350/>

* **長按**（LongClick）筆記列表的任一筆記進入編輯模式，選擇筆記加到新分類板。
* 設定為 **公開** 的分類板會顯示在探索頁面，可供其他使用者瀏覽與收藏。

<br>

#### **分類板內容**

<img src="https://github.com/tzuxlin/Week-4/blob/master/boardDetail.gif" height=350/>

* 條列筆記列表，除了可以查看筆記內容，看到感興趣的分類板也能夠點擊右上角的按鈕加入收藏。

#### **分類板列表**

<img src="https://github.com/tzuxlin/Week-4/blob/master/viewpager.gif" height=350/>

* 以 ViewPager 分別顯示 **使用者自行建立的分類板** 以及 **已收藏的公開分類板**。
* 運用 RecyclerView 呈現列表，使用者可以選擇 **條列式佈局（List）** 或 **網格式佈局（Grid）**。
* 點擊 Navigation Drawer 中的選單項目，可以進一步篩選畫面顯示的分類板列表。

<br>

<h3 id="part3">  3. 除了建立自己的內容，還可以進行個人化的探索 </h3>

<img src="https://github.com/tzuxlin/Week-4/blob/master/explore.gif" height=350/> <img src="https://github.com/tzuxlin/Week-4/blob/master/profile.gif" height=350/>


* `熱門` ：依照收藏數排列的熱門分類板。
* `推薦` ：依照使用者追蹤的 **主題標籤（tag）**，推薦相同內容的分類板。
* 以 **ChipGroup** 顯示使用者追蹤的主題標籤，並提供給使用者進行個人化的修改。

<br>

<h2 id="develop">開發資訊</h2>

### 框架及工具
- 程式語言
  - Kotlin
  
- 設計模式
  - MVVM with ViewModel, LiveData and Data Binding

- 實作功能
  - Linear / Grid / Waterfall RecyclerView
  - ViewPager
  - ChipGroup
  
- Tools
  - Jsoup
  - Glide
  - Firebase Firestore
  - Firebase Crashlytics
  - Espresso
  - Youtube Player API
  - Google Maps SDK
  - Facebook SDK
  
### 系統需求
- Android 7.0+ (Android SDK 24+)
- Android Studio 4.0

### 最新版本
- 1.1.1

### 聯絡方式
Tzu Hsuan Lin, tzuxlin@gmail.com 
