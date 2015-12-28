package megascripts.api;

import java.util.HashSet;

import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.map.LocalPath.Vertex;
import org.powerbot.game.api.wrappers.map.TilePath;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Calc {


	public static int dijkstraDist(final int startX, final int startY,
			final int destX, final int destY, final boolean isObject) {
		try {
			final int[][] prev = new int[104][104];
			final int[][] dist = new int[104][104];
			final int[] path_x = new int[4000];
			final int[] path_y = new int[4000];
			for (int xx = 0; xx < 104; xx++) {
				for (int yy = 0; yy < 104; yy++) {
					prev[xx][yy] = 0;
					dist[xx][yy] = 99999999;
				}
			}
			int curr_x = startX;
			int curr_y = startY;
			prev[startX][startY] = 99;
			dist[startX][startY] = 0;
			int path_ptr = 0;
			int step_ptr = 0;
			path_x[path_ptr] = startX;
			path_y[path_ptr++] = startY;
			final int blocks[][] = Walking.getCollisionFlags(Game.getPlane());
			final int pathLength = path_x.length;
			boolean foundPath = false;
			while (step_ptr != path_ptr) {
				curr_x = path_x[step_ptr];
				curr_y = path_y[step_ptr];
				if (Math.abs(curr_x - destX) + Math.abs(curr_y - destY) == (isObject ? 1
						: 0)) {
					foundPath = true;
					break;
				}
				step_ptr = (step_ptr + 1) % pathLength;
				final int cost = dist[curr_x][curr_y] + 1;
				// south
				if (curr_y > 0 && prev[curr_x][curr_y - 1] == 0
						&& (blocks[curr_x + 1][curr_y] & 0x1280102) == 0) {
					path_x[path_ptr] = curr_x;
					path_y[path_ptr] = curr_y - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x][curr_y - 1] = 1;
					dist[curr_x][curr_y - 1] = cost;
				}
				// west
				if (curr_x > 0 && prev[curr_x - 1][curr_y] == 0
						&& (blocks[curr_x][curr_y + 1] & 0x1280108) == 0) {
					path_x[path_ptr] = curr_x - 1;
					path_y[path_ptr] = curr_y;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x - 1][curr_y] = 2;
					dist[curr_x - 1][curr_y] = cost;
				}
				// north
				if (curr_y < 104 - 1 && prev[curr_x][curr_y + 1] == 0
						&& (blocks[curr_x + 1][curr_y + 2] & 0x1280120) == 0) {
					path_x[path_ptr] = curr_x;
					path_y[path_ptr] = curr_y + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x][curr_y + 1] = 4;
					dist[curr_x][curr_y + 1] = cost;
				}
				// east
				if (curr_x < 104 - 1 && prev[curr_x + 1][curr_y] == 0
						&& (blocks[curr_x + 2][curr_y + 1] & 0x1280180) == 0) {
					path_x[path_ptr] = curr_x + 1;
					path_y[path_ptr] = curr_y;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x + 1][curr_y] = 8;
					dist[curr_x + 1][curr_y] = cost;
				}
				// south west
				if (curr_x > 0 && curr_y > 0
						&& prev[curr_x - 1][curr_y - 1] == 0
						&& (blocks[curr_x][curr_y] & 0x128010e) == 0
						&& (blocks[curr_x][curr_y + 1] & 0x1280108) == 0
						&& (blocks[curr_x + 1][curr_y] & 0x1280102) == 0) {
					path_x[path_ptr] = curr_x - 1;
					path_y[path_ptr] = curr_y - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x - 1][curr_y - 1] = 3;
					dist[curr_x - 1][curr_y - 1] = cost;
				}
				// north west
				if (curr_x > 0 && curr_y < 104 - 1
						&& prev[curr_x - 1][curr_y + 1] == 0
						&& (blocks[curr_x][curr_y + 2] & 0x1280138) == 0
						&& (blocks[curr_x][curr_y + 1] & 0x1280108) == 0
						&& (blocks[curr_x + 1][curr_y + 2] & 0x1280120) == 0) {
					path_x[path_ptr] = curr_x - 1;
					path_y[path_ptr] = curr_y + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x - 1][curr_y + 1] = 6;
					dist[curr_x - 1][curr_y + 1] = cost;
				}
				// south east
				if (curr_x < 104 - 1 && curr_y > 0
						&& prev[curr_x + 1][curr_y - 1] == 0
						&& (blocks[curr_x + 2][curr_y] & 0x1280183) == 0
						&& (blocks[curr_x + 2][curr_y + 1] & 0x1280180) == 0
						&& (blocks[curr_x + 1][curr_y] & 0x1280102) == 0) {
					path_x[path_ptr] = curr_x + 1;
					path_y[path_ptr] = curr_y - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x + 1][curr_y - 1] = 9;
					dist[curr_x + 1][curr_y - 1] = cost;
				}
				// north east
				if (curr_x < 104 - 1 && curr_y < 104 - 1
						&& prev[curr_x + 1][curr_y + 1] == 0
						&& (blocks[curr_x + 2][curr_y + 2] & 0x12801e0) == 0
						&& (blocks[curr_x + 2][curr_y + 1] & 0x1280180) == 0
						&& (blocks[curr_x + 1][curr_y + 2] & 0x1280120) == 0) {
					path_x[path_ptr] = curr_x + 1;
					path_y[path_ptr] = curr_y + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[curr_x + 1][curr_y + 1] = 12;
					dist[curr_x + 1][curr_y + 1] = cost;
				}
			}
			return foundPath ? dist[curr_x][curr_y] : -1;
		} catch (Exception e) {
			return -1;
		}

	}
	
	public static boolean Reach(SceneObject Object) {
		return (Object  !=null)&& (dijkstraDist(Players.getLocal().getRegionOffset().getX(),
				Players.getLocal().getRegionOffset().getY(), Object
						.getRegionOffset().getX(), Object.getRegionOffset()
						.getY(), true) != -1);
	}
	public static boolean Reach(Tile Object) {
		return (Object  !=null)&& (dijkstraDist(Players.getLocal().getRegionOffset().getX(),
				Players.getLocal().getRegionOffset().getY(), Object
						.getRegionOffset().getX(), Object.getRegionOffset()
						.getY(), true) != -1);
	}
	public static boolean Reach(NPC Object) {
		return (Object  !=null)&& (dijkstraDist(Players.getLocal().getRegionOffset().getX(), Players
				.getLocal().getRegionOffset().getY(), Object.getRegionOffset()
				.getX(), Object.getRegionOffset().getY(), true) != -1);
	}

	public static boolean Reach(GroundItem Object) {
		return (Object  !=null)&&(dijkstraDist(Players.getLocal().getRegionOffset().getX(), Players
				.getLocal().getRegionOffset().getY(), Object.getRegionOffset()
				.getX(), Object.getRegionOffset().getY(), true) != -1);
	}
	public static boolean Reach(Tile Start,Tile end) {
		return (dijkstraDist(Start.getRegionOffset().getX(), Start.getRegionOffset().getY(), end.getRegionOffset()
				.getX(), end.getRegionOffset().getY(), true) != -1);
	}
}
