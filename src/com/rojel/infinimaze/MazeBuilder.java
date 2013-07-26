package com.rojel.infinimaze;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.World;

import com.rojel.infinimaze.MazeBlock.Wall;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class MazeBuilder {
	MazeBlock[][] maze;
	
	public void buildMaze(int width, int height) {
		maze = new MazeBlock[width][height];
		
		for(int x = 0; x < maze.length; x++) {
			for(int y = 0; y < maze[0].length; y++) {
				maze[x][y] = new MazeBlock(x, y);
				
				if(x == 0)
					maze[x][y].setBorder(Wall.WEST, true);
				if(x == width - 1)
					maze[x][y].setBorder(Wall.EAST, true);
				if(y == 0)
					maze[x][y].setBorder(Wall.NORTH, true);
				if(y == height - 1)
					maze[x][y].setBorder(Wall.SOUTH, true);
			}
		}
		
		Stack<MazeBlock> cellStack = new Stack<MazeBlock>();
		int totalCells = width * height;
		int x = (int) (Math.random() * width);
		int y = (int) (Math.random() * height);
		int visitedCells = 1;
		
		while(visitedCells < totalCells) {
			ArrayList<MazeBlock> intactNeighbors = new ArrayList<MazeBlock>();
			if(!maze[x][y].getBorder(Wall.WEST) && maze[x - 1][y].isClosed())
				intactNeighbors.add(maze[x - 1][y]);
			if(!maze[x][y].getBorder(Wall.EAST) && maze[x + 1][y].isClosed())
				intactNeighbors.add(maze[x + 1][y]);
			if(!maze[x][y].getBorder(Wall.NORTH) && maze[x][y - 1].isClosed())
				intactNeighbors.add(maze[x][y - 1]);
			if(!maze[x][y].getBorder(Wall.SOUTH) && maze[x][y + 1].isClosed())
				intactNeighbors.add(maze[x][y + 1]);
			
			if(intactNeighbors.size() > 0) {
				MazeBlock randomNeighbor = intactNeighbors.get((int) (Math.random() * intactNeighbors.size()));
				
				randomNeighbor.connect(maze[x][y]);
				cellStack.push(maze[x][y]);
				x = randomNeighbor.getX();
				y = randomNeighbor.getY();
				visitedCells++;
			} else {
				MazeBlock lastCell = cellStack.pop();
				x = lastCell.getX();
				y = lastCell.getY();
			}
		}
	}
	
	public void placeInWorld(World world, Location corner) {
		BukkitWorld bukkitWorld = new BukkitWorld(world);
		EditSession es = new EditSession(bukkitWorld, Integer.MAX_VALUE);
		
		for(int x = 0; x < maze.length; x++) {
			for(int y = 0; y < maze[0].length; y++) {
				CuboidClipboard cc = loadClipboardForBlock(maze[x][y]);
				try {
					cc.paste(es, new Vector(x * 7 + corner.getBlockX(), corner.getBlockY(), y * 7 + corner.getBlockZ()), false);
				} catch(MaxChangedBlocksException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public CuboidClipboard loadClipboardForBlock(MazeBlock block) {
		CuboidClipboard cc = new CuboidClipboard(new Vector(7, 7, 7));
		
		if(block.numberOfWalls() == 0) {
			cc = loadSchematic("cross.schematic");
			cc.setOrigin(new Vector(3, 3, 3));
		}
		if(block.numberOfWalls() == 1) {
			cc = loadSchematic("junction.schematic");
			cc.setOrigin(new Vector(3, 3, 3));
			if(block.getWall(Wall.WEST))
				cc.rotate2D(180);
			if(block.getWall(Wall.SOUTH))
				cc.rotate2D(90);
			if(block.getWall(Wall.NORTH))
				cc.rotate2D(270);
		}
		if(block.numberOfWalls() == 2) {
			if((block.getWall(Wall.NORTH) && block.getWall(Wall.SOUTH)) || (block.getWall(Wall.EAST) && block.getWall(Wall.WEST))) {
				cc = loadSchematic("tunnel.schematic");
				cc.setOrigin(new Vector(3, 3, 3));
				if(block.getWall(Wall.EAST))
					cc.rotate2D(90);
			} else {
				cc = loadSchematic("corner.schematic");
				cc.setOrigin(new Vector(3, 3, 3));
				if(block.getWall(Wall.EAST) && block.getWall(Wall.SOUTH))
					cc.rotate2D(90);
				if(block.getWall(Wall.WEST) && block.getWall(Wall.SOUTH))
					cc.rotate2D(180);
				if(block.getWall(Wall.WEST) && block.getWall(Wall.NORTH))
					cc.rotate2D(270);
			}
		}
		
		return cc;
	}
	
	public CuboidClipboard loadSchematic(String fileName) {
		CuboidClipboard cc = new CuboidClipboard(new Vector(7, 7, 7));
		
		try {
			cc = SchematicFormat.MCEDIT.load(new File(Infinimaze.getPlugin().getDataFolder() + fileName));
		} catch(DataException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return cc;
	}
}
