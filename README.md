## 关于

⭐中点站 - 安卓悬浮窗文件中转站/暂存站 

目前未开发完成.. demo版 下面是一些截图

| 截图A | 截图B | 
|------|--------|
|  <img src="https://github.com/user-attachments/assets/a2be385f-10d8-43f1-8acc-c098370ffc4d" width="200"/>  | <img src="https://github.com/user-attachments/assets/d0bbd003-2cac-471b-9608-12be54abbe51" width="200" /> |

## 开发

启动流程图

![流程图](https://github.com/wooze-pao/mid-point/blob/main/pictures/pp1.png)

为什么不用Service作为悬浮窗载体？ 

-- 因为安卓拖入拖出（Drag And Drop）API需要从``` activity?.requestDragAndDropPermissions(event.toAndroidDragEvent()) ```获取权限，所以没有找到好的方法解决

