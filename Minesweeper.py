from random import randint
class Minesweeper:
    
    nx=0
    ny=0
    nmines=0
    grid=[[]]
    selected=[[]]
    flagged=[[]]
    
    def __init__(self, nx=10, ny=10, nmines=10):
        
        self.nx=nx
        self.ny=ny
        self.nmines=nmines
        
        self.grid=[[0 for j in range(ny)] for i in range(nx)]
        self.selected=[[False for j in range(ny)] for i in range(nx)]
        self.flagged=[[False for j in range(ny)] for i in range(nx)]
      
        for i in range(nmines):
            fl=0
            while (fl==0):
                x=randint(0, nx-1)
                y=randint(0, ny-1)
                if (self.grid[x][y]==0):
                    fl=1
                    self.grid[x][y]=-1
        
        # here put in the rest of the numbers
        for i in range(nx):
            for j in range(ny):
                if (self.grid[i][j]==-1):
                    continue
                x1=max(i-1,0)
                x2=min(i+1,nx-1)
                y1=max(j-1,0)
                y2=min(j+1,ny-1)
                for x0 in range(x1,x2+1):
                    for y0 in range(y1,y2+1):
                        if (self.grid[x0][y0]==-1):
                            self.grid[i][j]=self.grid[i][j]+1
                
    def select(self, x, y):
        self.selected[x][y]=True
        self.flagged[x][y]=False
        if (self.grid[x][y]==0):
            x1=max(x-1,0)
            x2=min(x+1,self.nx-1)
            y1=max(y-1,0)
            y2=min(y+1,self.ny-1)
            print(x1,x2,y1,y2)
            for i in range(x1,x2+1):
                for j in range(y1,y2+1):
                    if (not self.selected[i][j]):
                        z=self.select(i,j)
                        if (z==-1): # something went wrong
                            exit
        
        return self.grid[x][y]
    
    def textoutput(self):
        
        if (self.nx>9):
            # print tens line
            s="  "
            for i in range(self.nx):
                if (i<10):
                    s=s+"  "
                else:
                    s=s+" %1d" % (i/10)
            print(s)
            # print ones line
            s="  "
            for i in range(self.nx):
                s=s+" %1d" % (i%10)
            print(s)
            
            for j in range(self.ny):
                s="%2d" % j
                for i in range(self.nx):
                    if (self.flagged[i][j]):
                        s=s+" !"
                        continue
                    if (not self.selected[i][j]):
                        s=s+" ?"
                        continue
                    if (self.grid[i][j]==0):
                        s=s+" ."
                        continue
                    s=s+" %1d" % self.grid[i][j]
                print(s)
    
    def playerwon(self):
        n=0
        for i in range(self.nx):
            for j in range(self.ny):
                if (not self.selected[i][j]):
                    n=n+1
                    if (n>self.nmines):
                        return False
                if ((self.selected[i][j]) and (self.grid[i][j]==-1)):
                    return False
        
        if (n==self.nmines):
            return True
        else:
            return False
        
a=Minesweeper(15,15,20)
won=False
while (not won):
    a.textoutput()
    xstr=input("Enter x of space to check:")
    x=int(xstr)
    ystr=input("Enter y of space to check:")
    y=int(ystr)
    z=a.select(x, y)
    if (z==-1):
        print("BOOM! You hit a mine!")
        exit
    won=a.playerwon()
    
print("You got all the mines!")
    