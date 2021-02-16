　　完整代码[https://github.com/huadongf/Bill2](https://github.com/huadongf/Bill2)  
　　纪念一下个人独立开发的功能全面的一款app——雨燕记账。这可能是你见过的最好看的记账app！该项目后续会继续完善并上架应用商店。  
　　完成的软件功能包括：（1）分类记录日常消费数据，包括金额，用途，日期，分类等信息，（2） 显示消费流水记录，（3）按天、周、月、年进行汇总分析形成结果。（4）能够根据输入条件查询，包括金额范围，用途，日期范围，分类等；（5）具备友好的用户数据输入界面和查询界面。  
　　为了保证软件与用户的交互友好性，本软件采用了底部菜单作为导航栏，并使用了jetpack的navigation组件来处理fragment之间的跳转，使用扇形图来汇总分析数据，使用对话框处理备注的输入和日期的输入，使用数据库来实现增删改查。  
　　软件中类的说明表如下

| 类  |  说明 |
| :------------: | :------------: |
|  DashboardFragment.kt |  “统计”页面的fragment |
|  DashboardViewModel.kt | 用来存放“统计”页面数据的viewmodel  |
|  HomeFragment.kt | “首页”的fragment  |
|  HomeViewModel.kt | 用来存放“首页”页面数据的viewmodel  |
|  NotificationsFragment.kt |  “我的”页面的fragment |
|  NotificationsViewModel.kt |  “我的”页面的viewmodel |
|  AddActivity.kt |  添加页面的activity |
|  AppDatabase.kt  |  提供直接访问底层数据库实现，我们应该从里面拿到具体的Dao,进行数据库操作。 |
|  Cate.kt | 用来保存分类的类  |
| DetailActivity.kt  |  详情页的activity |
| Homeadapter.kt  |  首页recyclerview的适配器 |
|  MainActivity.kt |  首页的activity |
| MyAdapter.kt  | 添加页面的recyclerview适配器  |
|  SearchableActivity.kt |  搜索页面的activity |
| Searchadapter.kt  | 搜索页面的recyclerview适配器  |
| User.kt  |  数据库具体的bean实体，会与数据库表column进行映射 |
| UserDao.kt  | 数据库访问对象，实现具体的增删改查  |


　　　　　　　　　　　　　　　　　　　　　　　　项目结构如下
![](https://huadongf.com/upload/20210131_11352720.png)
　　　　　　　　　　　　　　　　　　　　　　　　首页
![](https://huadongf.com/upload/20210131_1135592.png)
　　　　　　　　　　　　　　　　　　　　　　　　统计页
![](https://huadongf.com/upload/20210131_11361546.png)
　　　　　　　　　　　　　　　　　　　　　　　　添加页
![](https://huadongf.com/upload/20210131_1136407.png)
　　　　　　　　　　　　　　　　　　　　　　　　详情页
![](https://huadongf.com/upload/20210131_1136569.png)
