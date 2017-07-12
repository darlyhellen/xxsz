package com.xiangxun.xacity.view.treelist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.xiangxun.xacity.R;

/**
 * http://blog.csdn.net/lmj623565791/article/details/40212367
 * @author zhy
 *
 */
public class TreeHelper
{
	/**
	 * 传入我们的普通bean，转化为我们排序后的Node
	 * 
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> List<TreeNode> getSortedNodes(List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException

	{
		List<TreeNode> result = new ArrayList<TreeNode>();
		// 将用户数据转化为List<Node>
		List<TreeNode> nodes = convetData2Node(datas);
		// 拿到根节点
		List<TreeNode> rootNodes = getRootNodes(nodes);
		// 排序以及设置Node间关系
		for (TreeNode node : rootNodes)
		{
			addNode(result, node, defaultExpandLevel, 1);
		}
		return result;
	}

	/**
	 * 过滤出所有可见的Node
	 * 
	 * @param nodes
	 * @return
	 */
	public static List<TreeNode> filterVisibleNode(List<TreeNode> nodes)
	{
		List<TreeNode> result = new ArrayList<TreeNode>();

		for (TreeNode node : nodes)
		{
			// 如果为跟节点，或者上层目录为展开状态
			if (node.isRoot() || node.isParentExpand())
			{
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 将我们的数据转化为树的节点
	 * 
	 * @param datas
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static <T> List<TreeNode> convetData2Node(List<T> datas)
			throws IllegalArgumentException, IllegalAccessException

	{
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		TreeNode node = null;

		for (T t : datas)
		{
			int id = -1;
			int pId = -1;
			String label = null;
			Object data = null;
			Class<? extends Object> clazz = t.getClass();
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field f : declaredFields)
			{
				if (f.getAnnotation(TreeNodeId.class) != null)
				{
					f.setAccessible(true);
					id = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodePid.class) != null)
				{
					f.setAccessible(true);
					pId = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodeLabel.class) != null)
				{
					f.setAccessible(true);
					label = (String) f.get(t);
				}
				if (f.getAnnotation(TreeNodeData.class) != null)
				{
					f.setAccessible(true);
					data = (Object) f.get(t);
				}
				
				if (id != -1 && pId != -1 && label != null)
				{
					break;
				}
			}
			node = new TreeNode(id, pId, label, data);
			nodes.add(node);
		}
		
		/**
		 * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
		 */
		for (int i = 0; i < nodes.size(); i++)
		{
			TreeNode n = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++)
			{
				TreeNode m = nodes.get(j);
				if (m.getpId() == n.getId())
				{
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getpId())
				{
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}

		// 设置图片
		for (TreeNode n : nodes)
		{
			setNodeIcon(n);
		}
		return nodes;
	}

	private static List<TreeNode> getRootNodes(List<TreeNode> nodes)
	{
		List<TreeNode> root = new ArrayList<TreeNode>();
		for (TreeNode node : nodes)
		{
			if (node.isRoot())
				root.add(node);
		}
		return root;
	}

	/**
	 * 把一个节点上的所有的内容都挂上去
	 */
	private static void addNode(List<TreeNode> nodes, TreeNode node,
			int defaultExpandLeval, int currentLevel)
	{

		nodes.add(node);
		if (defaultExpandLeval >= currentLevel)
		{
			node.setExpand(true);
		}

		if (node.isLeaf())
			return;
		for (int i = 0; i < node.getChildren().size(); i++)
		{
			addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
					currentLevel + 1);
		}
	}

	/**
	 * 设置节点的图标
	 * 
	 * @param node
	 */
	private static void setNodeIcon(TreeNode node)
	{
		if (node.getChildren().size() > 0 && node.isExpand())
		{
			node.setIcon(R.drawable.ex_up);
		} else if (node.getChildren().size() > 0 && !node.isExpand())
		{
			node.setIcon(R.drawable.ex_down_def);
		} else
			node.setIcon(-1);

	}

}
