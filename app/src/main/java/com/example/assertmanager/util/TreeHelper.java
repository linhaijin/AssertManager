package com.example.assertmanager.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.assertmanager.annotation.TreeNodeId;
import com.example.assertmanager.annotation.TreeNodeLabel;
import com.example.assertmanager.annotation.TreeNodePId;
import com.example.myqr_codescan.R;

/**
 * Created by Xingfeng on 2015/6/8.
 */
public class TreeHelper {

	/*
	 * 将用户的数据转化为树形数据
	 */
	private static <T> List<Node> convertDatas2Nodes(List<T> datas)
			throws IllegalAccessException {

		List<Node> nodes = new ArrayList<Node>();

		Node node = null;
		for (T t : datas) {

			int id = -1;
			int pid = -1;
			String label = null;

			Class clazz = t.getClass();
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {

				if (field.getAnnotation(TreeNodeId.class) != null) {

					field.setAccessible(true);
					id = field.getInt(t);

				}
				if (field.getAnnotation(TreeNodePId.class) != null) {

					field.setAccessible(true);
					pid = field.getInt(t);

				}
				if (field.getAnnotation(TreeNodeLabel.class) != null) {

					field.setAccessible(true);
					label = (String) field.get(t);

				}

			}

			node = new Node(id, pid, label);
			nodes.add(node);

		}

		Node n = null;
		Node m = null;
		/*
		 * 设置node间的关系
		 */
		for (int i = 0; i < nodes.size() - 1; i++) {

			n = nodes.get(i);

			for (int j = i + 1; j < nodes.size(); j++) {

				m = nodes.get(j);

				if (m.getPid() == n.getId()) {
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getPid()) {
					m.getChildren().add(n);
					n.setParent(m);
				}

			}

		}

		for (Node n1 : nodes) {

			setNodeIcon(n1);
			n1 = null;

		}

		return nodes;

	}

	/*
	 * 为Node设置图标
	 */
	private static void setNodeIcon(Node n) {

		if (n.getChildren().size() > 0 && n.isExpand()) {
			n.setIcon(R.drawable.tree_ex);
		} else if (n.getChildren().size() > 0 && !n.isExpand()) {
			n.setIcon(R.drawable.tree_ec);
		} else
			n.setIcon(-1);

	}

	public static <T> List<Node> getSortedNodes(List<T> datas,
			int defaultExpandLevel) throws IllegalAccessException {

		List<Node> result = new ArrayList<Node>();
		List<Node> nodes = convertDatas2Nodes(datas);

		// get root nodesl
		List<Node> rootNodes = getRootNodes(nodes);

		for (Node node : rootNodes) {

			addNodes(result, node, defaultExpandLevel, 1);

			node = null;

		}
		return result;

	}

	/*
	 * 设置出可见节点
	 */
	public static List<Node> filterVisibleNodes(List<Node> nodes) {

		List<Node> result = new ArrayList<Node>();

		for (Node node : nodes) {

			if (node.isRoot() || node.isParentExpand()) {

				setNodeIcon(node);
				result.add(node);

			}

			node = null;

		}

		return result;

	}

	/*
	 * 把一个节点的所有孩子节点都放入result中
	 */
	private static void addNodes(List<Node> result, Node node,
			int defaultExpandLevel, int currentLevel) {

		result.add(node);
		
		if (defaultExpandLevel > currentLevel) {

			node.setExpand(true);

		}
		if (node.isLeaf())
			return;

		for (int i = 0; i < node.getChildren().size(); i++) {

			addNodes(result, node.getChildren().get(i), defaultExpandLevel,
					currentLevel + 1);

		}

		node = null;

	}

	/*
	 * 从数据中获取根节点
	 */
	private static List<Node> getRootNodes(List<Node> nodes) {

		List<Node> rootNodes = new ArrayList<Node>();

		for (Node node : nodes) {

			if (node.isRoot())
				rootNodes.add(node);

			node = null;

		}

		return rootNodes;
	}

}
