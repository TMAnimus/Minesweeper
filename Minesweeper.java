//import java.lang.*;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
	int nx;
	int ny;
	int nmines;
	int grid[][];
	boolean selected[][];

	Minesweeper(int nx0, int ny0, int nm0) throws IndexOutOfBoundsException {
		int i, j, x0, y0;
		Random rnd=new Random();
		if (nx0>0) {
			nx=nx0;
		} else {
			throw new IndexOutOfBoundsException();
		}
		if (ny0>0) {
			ny=ny0;
		} else {
			throw new IndexOutOfBoundsException();
		}
		if ((nm0>0) && (nm0<nx*ny)) {
			nmines=nm0;
		} else {
			throw new IndexOutOfBoundsException();
		}

		grid=new int[nx][ny];
		selected=new boolean[nx][ny];

		// First put in the mines... each one is marked by a -1
		for (i=0; i<nmines; i++) {
			j=0;
			while (j==0) {
				x0=(int) (nx*rnd.nextDouble());
				y0=(int) (ny*rnd.nextDouble());
				if (grid[x0][y0]==0) {
					grid[x0][y0]=-1;
					j=1;
				}
			}
		}

		// Now put in the mine numbers 
		for (i=0; i<nx; i++) {
			for (j=0; j<ny; j++) {
				if (grid[i][j]==-1) continue;
				int x1=(i==0)?0:i-1;
				int x2=(i==nx-1)?nx-1:i+1;
				int y1=(j==0)?0:j-1;
				int y2=(j==ny-1)?ny-1:j+1;
				for (x0=x1; x0<=x2; x0++) 
					for (y0=y1; y0<=y2; y0++)
						if (grid[x0][y0]==-1) grid[i][j]++;
			}
		}
	}

	public int select(int x0, int y0) throws IndexOutOfBoundsException {
		// returns -1 if the player blew up, else number of space
		// Also handles 'spreading' if player selects a zero space
		int i,j,k,x1,y1,x2,y2;

		if ((x0<0) || (x0>nx-1) || (y0<0) || (y0>ny-1))
			throw new IndexOutOfBoundsException();

		selected[x0][y0]=true;
		if (grid[x0][y0]==0) {
			x1=(x0==0)?0:x0-1;
			x2=(x0==nx-1)?nx-1:x0+1;
			y1=(y0==0)?0:y0-1;
			y2=(y0==ny-1)?ny-1:y0+1;
			for (i=x1; i<=x2; i++)
				for (j=y1; j<=y2; j++)
					if (!selected[i][j]) {
						k=this.select(i,j);
						if (k==-1) throw new IndexOutOfBoundsException();
					}
		}
		return grid[x0][y0];
	}

	public boolean playerwon() {
		int i,j;
		int nspacesleft=0;
		for (i=0; i<nx; i++)
			for (j=0; j<ny; j++)
				if (!selected[i][j]) {
					if (++nspacesleft > nmines) return false;
				}
		// double-check
		if (nspacesleft==nmines) return true;
		return false;
	}

	public void textdisplay() {
		int i,j;
		if (nx>9) {
			System.out.print("  ");
			for (i=0; i<nx; i++) {
				if (i<10) {
					System.out.print("  ");
				} else {
					j=i/10;
					System.out.print(" ");
					System.out.print(Integer.toString(j));
				}
			}
			System.out.println();
		}
		System.out.print("  ");
		for (i=0; i<nx; i++) {
			j=i%10;
			System.out.print(" ");
			System.out.print(Integer.toString(j));
		}
		System.out.println();
		System.out.println();

		for (j=0; j<ny; j++) {
			if (j<10) {
				System.out.print(" "+Integer.toString(j));
			} else {
				System.out.print(Integer.toString(j));
			}
			for (i=0; i<nx; i++) {
				System.out.print(" ");
				if (selected[i][j]) {
					System.out.print(Integer.toString(grid[i][j]));
				} else {
					System.out.print("?");
				}
			}
			System.out.println();
		}			
	}

	public static void main(String args[]) {
		Minesweeper a;
		boolean won=false;
		int i,j,k;
		Scanner reader=new Scanner(System.in);

		a=new Minesweeper(15,15,12);
		
		do {
			a.textdisplay();
			System.out.println("Enter coordinates to check:");
			i=reader.nextInt();
			j=reader.nextInt();
			if ((i>=0) && (i<a.nx) && (j>=0) && (j<=a.ny)) {
				k=a.select(i,j);
				if (k==-1) {
					System.out.println("BOOM! You lose!");
					reader.close();
					System.exit(0);
				}
			}
			won=a.playerwon();
		} while (!won);
		System.out.println("You avoided all the mines!");
		a.textdisplay();
		reader.close();
	}

}