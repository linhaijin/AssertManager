package com.example.assertmanager.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingfeng on 2015/6/8.
 */
public class Node {

    private int id;
    private int pid=0;//根节点的pid-0
    private String name;
    private int level;//树的层级
    private boolean isExpand;//是否是展开
    private int icon;
    private Node parent;
    private List<Node> children=new ArrayList<Node>();

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {

        return icon;
    }
    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    /*
    得到当前代码的层级
     */
    public int getLevel() {

        return parent==null?0:parent.getLevel()+1;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Node(int id, int pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public void setExpand(boolean isExpand) {

    this.isExpand=isExpand;

        if(!isExpand){

            for(Node node:children){
                node.setExpand(false);
            }

        }

    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /*
    是否是根节点
     */
    public boolean isRoot(){
        return parent==null;
    }

    /*
    判断当前父节点的收缩状态
     */
    public boolean isParentExpand(){

        if(parent==null)
            return false;
        return parent.isExpand();

    }

    /*
    是否是叶节点
     */
    public boolean isLeaf(){

        return children.size()==0;

    }
}
