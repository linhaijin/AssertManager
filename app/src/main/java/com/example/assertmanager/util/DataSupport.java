package com.example.assertmanager.util;

import java.util.ArrayList;
import java.util.List;

import com.example.assertmanager.model.BmBean;
import com.example.assertmanager.model.CheckItemBean;
import com.example.assertmanager.model.GroupItemBean;
import com.example.assertmanager.model.KmBean;

/**
 * 提供部门和资产类型等数据
 * 
 * @author Xingfeng
 *
 */
public class DataSupport {

	private static List<BmBean> mDatas = null;

	private static List<KmBean> kmList = null;

	public static List<BmBean> getDepartmentList() {

		if (mDatas == null) {

			mDatas = new ArrayList<BmBean>();

			BmBean bean = new BmBean(1, 0, "新部门1");
			mDatas.add(bean);

			bean = new BmBean(2, 0, "新部门2");
			mDatas.add(bean);

			bean = new BmBean(3, 0, "新部门3");
			mDatas.add(bean);

			bean = new BmBean(4, 1, "新部门11");
			mDatas.add(bean);

			bean = new BmBean(5, 2, "新部门21");
			mDatas.add(bean);

			bean = new BmBean(6, 3, "新部门31");
			mDatas.add(bean);

			bean = new BmBean(7, 3, "新部门32");
			mDatas.add(bean);

			bean = new BmBean(8, 3, "新部门33");
			mDatas.add(bean);

			bean = new BmBean(9, 6, "新部门311");
			mDatas.add(bean);

			bean = new BmBean(10, 6, "新部门312");
			mDatas.add(bean);

			bean = new BmBean(11, 7, "新部门321");
			mDatas.add(bean);

			bean = new BmBean(12, 7, "新部门322");
			mDatas.add(bean);

			bean = new BmBean(13, 7, "新部门323");
			mDatas.add(bean);

		}

		return mDatas;
	}

	static {

		if (kmList == null) {

			kmList = new ArrayList<KmBean>();

			KmBean assertBean = new KmBean(105, 0, "固定资产");
			kmList.add(assertBean);

			assertBean = new KmBean(1, 0, "房屋及建筑物");
			kmList.add(assertBean);

			assertBean = new KmBean(13, 1, "房屋");
			kmList.add(assertBean);
			assertBean = new KmBean(14, 1, "建筑物");
			kmList.add(assertBean);

			assertBean = new KmBean(15, 13, "办公用房");
			kmList.add(assertBean);
			assertBean = new KmBean(16, 13, "生活用房");
			kmList.add(assertBean);
			assertBean = new KmBean(17, 13, "招待用房");
			kmList.add(assertBean);
			assertBean = new KmBean(18, 13, "专业用房");
			kmList.add(assertBean);
			assertBean = new KmBean(19, 13, "会场、礼堂");
			kmList.add(assertBean);
			assertBean = new KmBean(20, 13, "车炮库");
			kmList.add(assertBean);
			assertBean = new KmBean(21, 13, "仓库");
			kmList.add(assertBean);
			assertBean = new KmBean(22, 13, "其他");
			kmList.add(assertBean);

			// 添加生活用房子类
			assertBean = new KmBean(23, 16, "集资房");
			kmList.add(assertBean);
			assertBean = new KmBean(24, 16, "安居工程房");
			kmList.add(assertBean);
			assertBean = new KmBean(25, 16, "经济适用房");
			kmList.add(assertBean);
			assertBean = new KmBean(26, 16, "公寓房");
			kmList.add(assertBean);
			assertBean = new KmBean(27, 16, "生活配套设施用房");
			kmList.add(assertBean);
			assertBean = new KmBean(28, 16, "其他");
			kmList.add(assertBean);

			// 添加建筑物子类
			assertBean = new KmBean(29, 14, "机场");
			kmList.add(assertBean);
			assertBean = new KmBean(30, 14, "码头");
			kmList.add(assertBean);
			assertBean = new KmBean(31, 14, "公路专用线(含桥梁)");
			kmList.add(assertBean);
			assertBean = new KmBean(32, 14, "铁路专用线(含桥梁)");
			kmList.add(assertBean);
			assertBean = new KmBean(33, 14, "工事");
			kmList.add(assertBean);
			assertBean = new KmBean(34, 14, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(2, 0, "一般设备");
			kmList.add(assertBean);

			// 添加一般设备子类
			assertBean = new KmBean(35, 2, "计算机");
			kmList.add(assertBean);
			assertBean = new KmBean(36, 2, "打印机");
			kmList.add(assertBean);
			assertBean = new KmBean(37, 2, "扫描机");
			kmList.add(assertBean);
			assertBean = new KmBean(38, 2, "刻录机");
			kmList.add(assertBean);
			assertBean = new KmBean(39, 2, "复印机");
			kmList.add(assertBean);
			assertBean = new KmBean(40, 2, "速印机");
			kmList.add(assertBean);
			assertBean = new KmBean(41, 2, "编辑机");
			kmList.add(assertBean);
			assertBean = new KmBean(42, 2, "刻字机");
			kmList.add(assertBean);
			assertBean = new KmBean(43, 2, "打字机");
			kmList.add(assertBean);
			assertBean = new KmBean(44, 2, "传真机");
			kmList.add(assertBean);
			assertBean = new KmBean(45, 2, "空调机");
			kmList.add(assertBean);
			assertBean = new KmBean(46, 2, "洗衣机");
			kmList.add(assertBean);
			assertBean = new KmBean(47, 2, "电冰箱");
			kmList.add(assertBean);
			assertBean = new KmBean(48, 2, "文件柜");
			kmList.add(assertBean);
			assertBean = new KmBean(49, 2, "保险箱");
			kmList.add(assertBean);
			assertBean = new KmBean(50, 2, "碎纸机");
			kmList.add(assertBean);
			assertBean = new KmBean(51, 2, "办公椅桌");
			kmList.add(assertBean);
			assertBean = new KmBean(52, 2, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(3, 0, "文体设备");
			kmList.add(assertBean);

			// 添加文体设备
			assertBean = new KmBean(53, 3, "乐器");
			kmList.add(assertBean);
			assertBean = new KmBean(54, 3, "电视机");
			kmList.add(assertBean);
			assertBean = new KmBean(55, 3, "功放机");
			kmList.add(assertBean);
			assertBean = new KmBean(56, 3, "录相机");
			kmList.add(assertBean);
			assertBean = new KmBean(57, 3, "VCD(DVD)机");
			kmList.add(assertBean);
			assertBean = new KmBean(58, 3, "照相机");
			kmList.add(assertBean);
			assertBean = new KmBean(59, 3, "摄像机");
			kmList.add(assertBean);
			assertBean = new KmBean(60, 3, "投影仪");
			kmList.add(assertBean);
			assertBean = new KmBean(61, 3, "体育器材");
			kmList.add(assertBean);
			assertBean = new KmBean(62, 3, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(4, 0, "运输设备");
			kmList.add(assertBean);
			// 添加运输设备
			assertBean = new KmBean(63, 4, "运输车");
			kmList.add(assertBean);
			assertBean = new KmBean(64, 4, "大轿车");
			kmList.add(assertBean);
			assertBean = new KmBean(65, 4, "小轿车");
			kmList.add(assertBean);
			assertBean = new KmBean(66, 4, "特种车");
			kmList.add(assertBean);
			assertBean = new KmBean(67, 4, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(5, 0, "通信设备");
			kmList.add(assertBean);
			// 添加通信设备
			assertBean = new KmBean(68, 5, "无线电话");
			kmList.add(assertBean);
			assertBean = new KmBean(69, 5, "有线电话");
			kmList.add(assertBean);
			assertBean = new KmBean(70, 5, "可视电话");
			kmList.add(assertBean);
			assertBean = new KmBean(71, 5, "寻呼机");
			kmList.add(assertBean);
			assertBean = new KmBean(72, 5, "对讲机");
			kmList.add(assertBean);
			assertBean = new KmBean(73, 5, "交换机");
			kmList.add(assertBean);
			assertBean = new KmBean(74, 5, "收(发)报机");
			kmList.add(assertBean);
			assertBean = new KmBean(75, 5, "载波机");
			kmList.add(assertBean);
			assertBean = new KmBean(76, 5, "电台");
			kmList.add(assertBean);
			assertBean = new KmBean(77, 5, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(6, 0, "医疗设备");
			kmList.add(assertBean);
			// 添加医疗设备
			assertBean = new KmBean(76, 6, "医疗器材");
			kmList.add(assertBean);
			assertBean = new KmBean(77, 6, "医疗仪器");
			kmList.add(assertBean);
			assertBean = new KmBean(78, 6, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(7, 0, "科研教学设备");
			kmList.add(assertBean);
			// 添加科研教学设备
			assertBean = new KmBean(79, 7, "教学设备");
			kmList.add(assertBean);
			assertBean = new KmBean(80, 7, "科研设备");
			kmList.add(assertBean);
			assertBean = new KmBean(81, 7, "训练设备");
			kmList.add(assertBean);

			assertBean = new KmBean(8, 0, "施工及修理设备");
			kmList.add(assertBean);
			// 添加施工及修理设备
			assertBean = new KmBean(82, 8, "施工设备");
			kmList.add(assertBean);
			assertBean = new KmBean(83, 8, "修理设备");
			kmList.add(assertBean);

			assertBean = new KmBean(9, 0, "装备维修设备");
			kmList.add(assertBean);
			// 添加装备维修设备
			assertBean = new KmBean(84, 9, "hhh装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(85, 9, "kkk装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(86, 9, "eee装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(87, 9, "lll装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(88, 9, "jjj装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(89, 9, "zzz装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(90, 9, "ccc装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(91, 9, "ttt装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(92, 9, "ggg装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(93, 9, "fff装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(94, 9, "xxx专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(95, 9, "yyy专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(96, 9, "zzz专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(97, 9, "mmm专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(98, 9, "nnn专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(99, 9, "kkk专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(100, 9, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(10, 0, "文物及陈列品");
			kmList.add(assertBean);
			// 添加文物及陈列品
			assertBean = new KmBean(101, 10, "文物");
			kmList.add(assertBean);
			assertBean = new KmBean(102, 10, "陈列品");
			kmList.add(assertBean);

			assertBean = new KmBean(11, 0, "图书及音像资料");
			kmList.add(assertBean);
			// 添加图书及音像资料
			assertBean = new KmBean(103, 11, "图书");
			kmList.add(assertBean);
			assertBean = new KmBean(104, 11, "音像资料");
			kmList.add(assertBean);

			assertBean = new KmBean(12, 0, "其他固定资产");
			kmList.add(assertBean);

		}

	}

	public static List<KmBean> getAssertTypeList() {

		if (kmList == null) {

			kmList = new ArrayList<KmBean>();

			KmBean assertBean = new KmBean(105, 0, "固定资产");
			kmList.add(assertBean);

			assertBean = new KmBean(1, 0, "房屋及建筑物");
			kmList.add(assertBean);

			assertBean = new KmBean(13, 1, "房屋");
			kmList.add(assertBean);
			assertBean = new KmBean(14, 1, "建筑物");
			kmList.add(assertBean);

			assertBean = new KmBean(15, 13, "办公用房");
			kmList.add(assertBean);
			assertBean = new KmBean(16, 13, "生活用房");
			kmList.add(assertBean);
			assertBean = new KmBean(17, 13, "招待用房");
			kmList.add(assertBean);
			assertBean = new KmBean(18, 13, "专业用房");
			kmList.add(assertBean);
			assertBean = new KmBean(19, 13, "会场、礼堂");
			kmList.add(assertBean);
			assertBean = new KmBean(20, 13, "车炮库");
			kmList.add(assertBean);
			assertBean = new KmBean(21, 13, "仓库");
			kmList.add(assertBean);
			assertBean = new KmBean(22, 13, "其他");
			kmList.add(assertBean);

			// 添加生活用房子类
			assertBean = new KmBean(23, 16, "集资房");
			kmList.add(assertBean);
			assertBean = new KmBean(24, 16, "安居工程房");
			kmList.add(assertBean);
			assertBean = new KmBean(25, 16, "经济适用房");
			kmList.add(assertBean);
			assertBean = new KmBean(26, 16, "公寓房");
			kmList.add(assertBean);
			assertBean = new KmBean(27, 16, "生活配套设施用房");
			kmList.add(assertBean);
			assertBean = new KmBean(28, 16, "其他");
			kmList.add(assertBean);

			// 添加建筑物子类
			assertBean = new KmBean(29, 14, "机场");
			kmList.add(assertBean);
			assertBean = new KmBean(30, 14, "码头");
			kmList.add(assertBean);
			assertBean = new KmBean(31, 14, "公路专用线(含桥梁)");
			kmList.add(assertBean);
			assertBean = new KmBean(32, 14, "铁路专用线(含桥梁)");
			kmList.add(assertBean);
			assertBean = new KmBean(33, 14, "工事");
			kmList.add(assertBean);
			assertBean = new KmBean(34, 14, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(2, 0, "一般设备");
			kmList.add(assertBean);

			// 添加一般设备子类
			assertBean = new KmBean(35, 2, "计算机");
			kmList.add(assertBean);
			assertBean = new KmBean(36, 2, "打印机");
			kmList.add(assertBean);
			assertBean = new KmBean(37, 2, "扫描机");
			kmList.add(assertBean);
			assertBean = new KmBean(38, 2, "刻录机");
			kmList.add(assertBean);
			assertBean = new KmBean(39, 2, "复印机");
			kmList.add(assertBean);
			assertBean = new KmBean(40, 2, "速印机");
			kmList.add(assertBean);
			assertBean = new KmBean(41, 2, "编辑机");
			kmList.add(assertBean);
			assertBean = new KmBean(42, 2, "刻字机");
			kmList.add(assertBean);
			assertBean = new KmBean(43, 2, "打字机");
			kmList.add(assertBean);
			assertBean = new KmBean(44, 2, "传真机");
			kmList.add(assertBean);
			assertBean = new KmBean(45, 2, "空调机");
			kmList.add(assertBean);
			assertBean = new KmBean(46, 2, "洗衣机");
			kmList.add(assertBean);
			assertBean = new KmBean(47, 2, "电冰箱");
			kmList.add(assertBean);
			assertBean = new KmBean(48, 2, "文件柜");
			kmList.add(assertBean);
			assertBean = new KmBean(49, 2, "保险箱");
			kmList.add(assertBean);
			assertBean = new KmBean(50, 2, "碎纸机");
			kmList.add(assertBean);
			assertBean = new KmBean(51, 2, "办公椅桌");
			kmList.add(assertBean);
			assertBean = new KmBean(52, 2, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(3, 0, "文体设备");
			kmList.add(assertBean);

			// 添加文体设备
			assertBean = new KmBean(53, 3, "乐器");
			kmList.add(assertBean);
			assertBean = new KmBean(54, 3, "电视机");
			kmList.add(assertBean);
			assertBean = new KmBean(55, 3, "功放机");
			kmList.add(assertBean);
			assertBean = new KmBean(56, 3, "录相机");
			kmList.add(assertBean);
			assertBean = new KmBean(57, 3, "VCD(DVD)机");
			kmList.add(assertBean);
			assertBean = new KmBean(58, 3, "照相机");
			kmList.add(assertBean);
			assertBean = new KmBean(59, 3, "摄像机");
			kmList.add(assertBean);
			assertBean = new KmBean(60, 3, "投影仪");
			kmList.add(assertBean);
			assertBean = new KmBean(61, 3, "体育器材");
			kmList.add(assertBean);
			assertBean = new KmBean(62, 3, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(4, 0, "运输设备");
			kmList.add(assertBean);
			// 添加运输设备
			assertBean = new KmBean(63, 4, "运输车");
			kmList.add(assertBean);
			assertBean = new KmBean(64, 4, "大轿车");
			kmList.add(assertBean);
			assertBean = new KmBean(65, 4, "小轿车");
			kmList.add(assertBean);
			assertBean = new KmBean(66, 4, "特种车");
			kmList.add(assertBean);
			assertBean = new KmBean(67, 4, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(5, 0, "通信设备");
			kmList.add(assertBean);
			// 添加通信设备
			assertBean = new KmBean(68, 5, "无线电话");
			kmList.add(assertBean);
			assertBean = new KmBean(69, 5, "有线电话");
			kmList.add(assertBean);
			assertBean = new KmBean(70, 5, "可视电话");
			kmList.add(assertBean);
			assertBean = new KmBean(71, 5, "寻呼机");
			kmList.add(assertBean);
			assertBean = new KmBean(72, 5, "对讲机");
			kmList.add(assertBean);
			assertBean = new KmBean(73, 5, "交换机");
			kmList.add(assertBean);
			assertBean = new KmBean(74, 5, "收(发)报机");
			kmList.add(assertBean);
			assertBean = new KmBean(75, 5, "载波机");
			kmList.add(assertBean);
			assertBean = new KmBean(76, 5, "电台");
			kmList.add(assertBean);
			assertBean = new KmBean(77, 5, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(6, 0, "医疗设备");
			kmList.add(assertBean);
			// 添加医疗设备
			assertBean = new KmBean(76, 6, "医疗器材");
			kmList.add(assertBean);
			assertBean = new KmBean(77, 6, "医疗仪器");
			kmList.add(assertBean);
			assertBean = new KmBean(78, 6, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(7, 0, "科研教学设备");
			kmList.add(assertBean);
			// 添加科研教学设备
			assertBean = new KmBean(79, 7, "教学设备");
			kmList.add(assertBean);
			assertBean = new KmBean(80, 7, "科研设备");
			kmList.add(assertBean);
			assertBean = new KmBean(81, 7, "训练设备");
			kmList.add(assertBean);

			assertBean = new KmBean(8, 0, "施工及修理设备");
			kmList.add(assertBean);
			// 添加施工及修理设备
			assertBean = new KmBean(82, 8, "施工设备");
			kmList.add(assertBean);
			assertBean = new KmBean(83, 8, "修理设备");
			kmList.add(assertBean);

			assertBean = new KmBean(9, 0, "装备维修设备");
			kmList.add(assertBean);
			// 添加装备维修设备
			assertBean = new KmBean(84, 9, "hhh装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(85, 9, "kkk装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(86, 9, "eee装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(87, 9, "lll装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(88, 9, "jjj装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(89, 9, "zzz装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(90, 9, "ccc装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(91, 9, "ttt装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(92, 9, "ggg装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(93, 9, "fff装备维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(94, 9, "xxx专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(95, 9, "yyy专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(96, 9, "zzz专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(97, 9, "mmm专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(98, 9, "nnn专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(99, 9, "kkk专用配套车底盘维修设备");
			kmList.add(assertBean);
			assertBean = new KmBean(100, 9, "其他");
			kmList.add(assertBean);

			assertBean = new KmBean(10, 0, "文物及陈列品");
			kmList.add(assertBean);
			// 添加文物及陈列品
			assertBean = new KmBean(101, 10, "文物");
			kmList.add(assertBean);
			assertBean = new KmBean(102, 10, "陈列品");
			kmList.add(assertBean);

			assertBean = new KmBean(11, 0, "图书及音像资料");
			kmList.add(assertBean);
			// 添加图书及音像资料
			assertBean = new KmBean(103, 11, "图书");
			kmList.add(assertBean);
			assertBean = new KmBean(104, 11, "音像资料");
			kmList.add(assertBean);

			assertBean = new KmBean(12, 0, "其他固定资产");
			kmList.add(assertBean);

		}

		return kmList;
	}

	private static List<GroupItemBean> groupList;

	public static List<GroupItemBean> getGroupInfo() {

		if (groupList == null) {
			groupList = new ArrayList<GroupItemBean>();

			groupList.add(new GroupItemBean(1, 0, "按科目分组"));
			groupList.add(new GroupItemBean(2, 0, "按部门分组"));
		}

		return groupList;

	}

	private static List<CheckItemBean> checkList;

	public static List<CheckItemBean> getCheckList() {

		if (checkList == null) {

			checkList = new ArrayList<CheckItemBean>();
			checkList.add(new CheckItemBean(1, 0, "未清查"));
			checkList.add(new CheckItemBean(2, 0, "已清查"));
			checkList.add(new CheckItemBean(3, 0, "全部"));

		}

		return checkList;

	}
}
