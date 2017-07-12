package com.xiangxun.xacity.view.treelist;

public class TreeFileBean
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	@TreeNodeData
	public Object data;
	
	public TreeFileBean(int _id, int parentId, String name, Object data)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
		this.data = data;
	}
}
