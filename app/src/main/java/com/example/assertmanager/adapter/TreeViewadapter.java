package com.example.assertmanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.assertmanager.util.Node;
import com.example.assertmanager.util.TreeHelper;

/**
 * Created by Xingfeng on 2015/6/8.
 */
public abstract class TreeViewadapter<T> extends BaseAdapter {

	private Context mContext;
	private List<Node> mAllNodes;
	protected List<Node> mVisibleNodes;
	protected LayoutInflater mInflater;
	private int mDefaultExpand;

	private ListView mTree;

	public interface OnTreeNodeClickListener {

		/**
		 * 
		 * @param node
		 * @param position
		 * @param type
		 *            0为单击，1为长按
		 */
		public void onClick(Node node, int position, int type);

	}

	private OnTreeNodeClickListener mListeener;

	public void setOnTreeNodeClickListeener(OnTreeNodeClickListener mListeener) {
		this.mListeener = mListeener;
	}

	public <T> TreeViewadapter(ListView mTree, Context context, List<T> datas,
			int defaultExpand) throws IllegalAccessException {

		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpand);
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

		this.mTree = mTree;
		mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				expandOrCollapse(position);
				if (mListeener != null)
					mListeener.onClick(mVisibleNodes.get(position), position, 0);

				mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
				notifyDataSetChanged();
			}
		});

		mTree.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				expandOrCollapse(position);
				if (mListeener != null)
					mListeener.onClick(mVisibleNodes.get(position), position, 1);
				mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
				notifyDataSetChanged();
				return true;
			}
		});

	}

	/*
	 * 点击收缩或者展开
	 */
	private void expandOrCollapse(int position) {
		Node n = mVisibleNodes.get(position);

		if (n != null) {

			if (n.isLeaf())
				return;

			n.setExpand(!n.isExpand());

		}
	}

	/*
	 * 动态插入节点
	 */
	public void addExtraNode(int position, String label) {

		Node node = mVisibleNodes.get(position);
		int indexOf = mAllNodes.indexOf(node);

		Node extraNode = new Node(mAllNodes.size(), indexOf, label);
		extraNode.setParent(node);
		node.getChildren().add(extraNode);
		mAllNodes.add(indexOf + 1, extraNode);

		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return mVisibleNodes.size();
	}

	@Override
	public Object getItem(int position) {
		return mVisibleNodes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Node node = mVisibleNodes.get(position);
		convertView = getConvertView(node, position, convertView, parent);
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);

		return convertView;
	}

	public abstract View getConvertView(Node node, int position,
			View convertView, ViewGroup parent);

}
