package com.rojel.infinimaze;

public class MazeBlock {
	public enum Wall {
		NORTH, SOUTH, EAST, WEST
	}
	
	private int x;
	private int y;
	
	private boolean north = true;
	private boolean south = true;
	private boolean east = true;
	private boolean west = true;
	
	private boolean northBorder = false;
	private boolean southBorder = false;
	private boolean eastBorder = false;
	private boolean westBorder = false;
	
	public MazeBlock(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setWall(Wall wall, boolean stands) {
		if(wall == Wall.NORTH)
			north = stands;
		if(wall == Wall.SOUTH)
			south = stands;
		if(wall == Wall.EAST)
			east = stands;
		if(wall == Wall.WEST)
			west = stands;
	}
	
	public boolean getWall(Wall wall) {
		if(wall == Wall.NORTH)
			return north;
		if(wall == Wall.SOUTH)
			return south;
		if(wall == Wall.EAST)
			return east;
		if(wall == Wall.WEST)
			return west;
		
		return false;
	}
	
	public void setBorder(Wall wall, boolean isBorder) {
		if(wall == Wall.NORTH)
			northBorder = isBorder;
		if(wall == Wall.SOUTH)
			southBorder = isBorder;
		if(wall == Wall.EAST)
			eastBorder = isBorder;
		if(wall == Wall.WEST)
			westBorder = isBorder;
	}
	
	public boolean getBorder(Wall wall) {
		if(wall == Wall.NORTH)
			return northBorder;
		if(wall == Wall.SOUTH)
			return southBorder;
		if(wall == Wall.EAST)
			return eastBorder;
		if(wall == Wall.WEST)
			return westBorder;
		
		return false;
	}
	
	public boolean isClosed() {
		if(!north)
			return false;
		if(!south)
			return false;
		if(!east)
			return false;
		if(!west)
			return false;
		
		return true;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void connect(MazeBlock otherBlock) {
		if(otherBlock.getX() < getX()) {
			otherBlock.setWall(Wall.EAST, false);
			setWall(Wall.WEST, false);
		}
		if(otherBlock.getX() > getX()) {
			otherBlock.setWall(Wall.WEST, false);
			setWall(Wall.EAST, false);
		}
		if(otherBlock.getY() < getY()) {
			otherBlock.setWall(Wall.SOUTH, false);
			setWall(Wall.NORTH, false);
		}
		if(otherBlock.getY() < getY()) {
			otherBlock.setWall(Wall.NORTH, false);
			setWall(Wall.SOUTH, false);
		}
	}
	
	public int numberOfWalls() {
		int number = 0;
		
		if(north)
			number++;
		if(south)
			number++;
		if(east)
			number++;
		if(west)
			number++;
		
		return number;
	}
}
