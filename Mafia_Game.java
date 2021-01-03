import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
public class Mafia_Game{
  public static abstract class Player
  {
    private int number;
    private int HP;
    Player(int i){number=i;setinHP();}
    public abstract void setinHP();   //abstract method for different HP
    public void setHP(int i){HP=i;}
    public void addHP(int i){HP+=i;}
    public int getHP(){return HP;}
    public void redHP(int i){if(HP-i>=0)HP-=i;else{HP=0;}}   //HP can't fall below 0 for any player
  }
  public static class Mafia extends Player
  {
    private Scanner in=new Scanner(System.in);
    Mafia(int i){super(i);setinHP();}
    public int chooseTarget(Player[] Players)
    {
      int hno=0;
      if(Players[0] instanceof Mafia)   //object comparison
      {
          do{
            System.out.print("Choose a target: ");
            boolean donee=true;
            while(donee){
            try{hno=in.nextInt();donee=false;}
            catch(InputMismatchException inp){System.out.println("Wrong input, enter again.");in.next();}}
            if(hno-1>Players.length || Players[hno-1]==null){System.out.println("Player not in game.");}
            if(Players[hno-1] instanceof Mafia){System.out.println("You cannot choose another Mafia.");}
        }while(hno-1>Players.length || Players[hno-1]==null || Players[hno-1] instanceof Mafia);
          return hno-1;
      }
      else{
      do
      {
        hno=RANDOM(0,Players.length);
      }while(Players[hno]==null || Players[hno] instanceof Mafia);
      return hno;}
    }
    public int kill(Player[] Players,int target,MyGenericList<Integer> Mafias)
    {
      int flag=-1;
      int sohp=sumofHP(Mafias,Players);
      int X=Players[target].getHP();      //has the HP of the target
      if(sohp>=X)
      {
          int Y=0;    //stores the number of mafias with non zero HP
          for(int i=0;i<Mafias.size();i++){if(Players[Mafias.get(i)]!=null && Players[Mafias.get(i)].getHP()>0){Y++;}}
          int FLAG=0; //FLAG=1 if any mafia's hp is less than X/Y and 0 otherwise
          for(int i=0;i<Mafias.size();i++){if(Players[Mafias.get(i)]!=null && Players[Mafias.get(i)].getHP()<X/Y){FLAG=1;break;}}
        if(FLAG==0)
              {for(int i=0;i<Mafias.size();i++){if(Players[Mafias.get(i)]!=null)Players[Mafias.get(i)].redHP(X/Y);}}
        else{
          int c=0;
              for(int i=0;i<Mafias.size();i++){
              if(Players[Mafias.get(i)]!=null && Players[Mafias.get(i)].getHP()<X/Y){   //change Mafias' HP to zero and HP of target is reduced
                Players[target].redHP(Players[Mafias.get(i)].getHP());
                Players[Mafias.get(i)].setHP(0);
                c++;
              }}
              int XX=Players[target].getHP(); //get the remaining HP of target
              for(int i=0;i<Mafias.size();i++){
                if(Players[Mafias.get(i)]!=null && Players[Mafias.get(i)].getHP()!=0){
                  Players[Mafias.get(i)].redHP(XX/(Mafias.size()-c));  //reduce the HP equally
                }}
        }
        Players[target].setHP(0);
        flag=0;
      }
      else{
        int Y=0;
        for(int i=0;i<Mafias.size();i++){if(Players[Mafias.get(i)]!=null && Players[Mafias.get(i)].getHP()>0){Y++;}}
        for(int i=0;i<Mafias.size();i++){
          if(Players[Mafias.get(i)]!=null && Y!=0 && Players[Mafias.get(i)].getHP()<=X/Y){Players[Mafias.get(i)].setHP(0);}
          else{
            if(Y!=0 && Players[Mafias.get(i)]!=null)Players[Mafias.get(i)].redHP(X/Y);}}
        Players[target].redHP(sohp);
        flag=1;
      }
      return flag;    //flag 0 means player will be killed and not killed ow
    }
    public int sumofHP(MyGenericList<Integer> Mafias,Player[] Players)
    {
      int sum=0;
      for(int i=0;i<Mafias.size();i++){if(Players[Mafias.get(i)]!=null){sum+=Players[Mafias.get(i)].getHP();}}
      return sum;
    }
    @Override
    public void setinHP(){setHP(2500);}       //overridden HP definiton
  }
  public static class Detective extends Player
  {
    Detective(int i){super(i);setinHP();}
    public int Identify(Player[] Players)     //returns an player number to be tested
    {
      Scanner in=new Scanner(System.in);
      int idn=0;
      if(Players[0] instanceof Detective)     //if the user is a detective
      {
        do{
          boolean dOne=true;
          System.out.print("Choose a player to test: ");
          while(dOne)
          {
            try{idn=in.nextInt();dOne=false;}
            catch(InputMismatchException inp){System.out.println("Wrong input, enter again.");in.next();}}
          if(Players[idn-1] instanceof Detective){System.out.println("You cannot test another detective.");}
          if(Players[idn-1]==null || idn-1>Players.length){System.out.println("Player not in game.");}
        }while(Players[idn-1]==null || Players[idn-1] instanceof Detective || idn-1>Players.length);
        return idn-1;
      }
      else{
        do{idn=RANDOM(0,Players.length);}while(Players[idn]==null || Players[idn] instanceof Detective);
        return idn;
    }
    }
    public boolean Test(Player[] Players,int i)   //checks if it is a mafia or not
    {return Players[i] instanceof Mafia;}
    @Override
    public void setinHP(){setHP(800);}      //overridden for abstract HP
  }
  public static class Healer extends Player
  {
    private Scanner in=new Scanner(System.in);
    Healer(int i){super(i);setinHP();}
    public int Heal(Player[] Players)
    {
      int hno=0;
      if(Players[0] instanceof Healer)
      {
          do{
            System.out.print("Choose a player to heal: ");
            boolean xoxo=true;
            while(xoxo)
            {
              try{hno=in.nextInt();xoxo=false;}
              catch(InputMismatchException inp){System.out.println("Wrong Input, enter again.");in.next();}}
            if(hno-1>Players.length || Players[hno-1]==null){System.out.println("Player not in game.");}
          }while(hno-1>Players.length || Players[hno-1]==null);
          return hno-1;
      }
      else{
      do
      {
        hno=RANDOM(0,Players.length);
      }while(Players[hno]==null);
      return hno;}
    }
    @Override
    public void setinHP(){setHP(800);}    //overridden the abstract HP
  }
  public static class Commoner extends Player
  {
    Commoner(int i){super(i);setinHP();}
    @Override
    public void setinHP(){setHP(1000);}    //overridden the abstract HP
  }
  public static class Vote
  {
    public int Voting(Player[] Players)
    {
        Scanner in=new Scanner(System.in);
        int a;
        int[] Votes=new int[Players.length];
        if(Players[0]!=null)    //getting the vote by the user only if user is alive
        {
          int soso=0;
          do{System.out.print("Select a person to vote out: ");
          boolean yoyo=true;
          while(yoyo)
          {try{soso=in.nextInt();yoyo=false;}
          catch(InputMismatchException inp){System.out.println("Wrong input, enter again.");in.next();}}
          if(soso-1>Players.length || Players[soso-1]==null){System.out.println("Player not in game");}
        }while(soso-1>Players.length || Players[soso-1]==null);
          Votes[soso-1]+=1;   //1 based indexing for the user and 0 for the code
        }
        //below is the code that takes care that the votes don't tie
        do{
        for(int i=1;i<Players.length;i++)
        {                               //CHECK LATER IF U WANT TO REASSIGN VOTES
          if(Players[i]!=null){         //take the vote of a player iff player is alive
          do{a=RANDOM(0,Players.length);}while(Players[a]==null);   //checking if the player is dead or alive
          Votes[a]+=1;}
        }}while(checkIftie(Votes));
        int max=Arrays.stream(Votes).max().getAsInt();
        for(int i=0;i<Votes.length;i++){if(Votes[i]==max){return i;}}
        return 0;
    }
    public boolean checkIftie(int[] Votes)
    {
      int maxx=Arrays.stream(Votes).max().getAsInt();
      int abd=0;
      for(int i=0;i<Votes.length;i++){if(Votes[i]==maxx){abd++;}}
      if(abd>1){return true;}
      return false;
    }
  }
  public static class Rounds
  {
    public Player[] startNewRound(MyGenericList<Integer> Mafias,MyGenericList<Integer> Detectives,MyGenericList<Integer> Healers,MyGenericList<Integer> Commoners,Player[] Players)
    {
      Mafia m=new Mafia(0);
      int target=m.chooseTarget(Players);
      System.out.println("Mafias have chosen their target.");
      Detective d=new Detective(0);
      int test=d.Identify(Players);
      System.out.println("Detectives have chosen a player to test.");
      if(Players[0] instanceof Detective){if(d.Test(Players,test)){System.out.println("Player "+Integer.toString(test+1)+" is a Mafia and is voted out.");}}
      else{if(Players[0] instanceof Detective)System.out.println("Player "+Integer.toString(test+1)+" is not a Mafia.");}
      Healer h=new Healer(0);
      int healt=h.Heal(Players);
      System.out.println("Healers have chosen someone to heal");
      Commoner c=new Commoner(0);
      int killstatus=m.kill(Players,target,Mafias);   //HP of the player is reduced
      Players[healt].addHP(500);
      if(killstatus==0){if(Players[target].getHP()==0)Players[target]=null;}  //if player is killed by mafia and not healed then it dies and doesn't participate in voting
      Vote v=new Vote();
      int voteout=v.Voting(Players);
      System.out.println("---End of actions---");
      //m.kill(Players,target,Mafias);
      if(d.Test(Players,test)){Players[test]=null;    //mafia out of game if identified by detective
        System.out.println("Player "+Integer.toString(test+1)+" has been tested and voted out. It is a Mafia.");}  //what happens if mafia gets killed, what about the killing?
      else
      {
        //Players[healt].addHP(500);    //healer adds 500 HP points
        if(killstatus==0)
        {if(Players[target]==null){//Players[target]=null;   //player killed by mafia
        System.out.println("Player "+Integer.toString(target+1)+" has been killed by Mafia.");}}
        Players[voteout]=null;
        System.out.println("Player "+Integer.toString(voteout+1)+" has been voted out.");
      }
      return Players;
    }
  }
  public static class SelectCharacter
  {
    private MyGenericList<Integer> Mafias=new MyGenericList<Integer>();
    private MyGenericList<Integer> Detectives=new MyGenericList<Integer>();
    private MyGenericList<Integer> Healers=new MyGenericList<Integer>();
    private MyGenericList<Integer> Commoners=new MyGenericList<Integer>();
    //below are the public getters
    public MyGenericList<Integer> getM(){return Mafias;}
    public MyGenericList<Integer> getD(){return Detectives;}
    public MyGenericList<Integer> getH(){return Healers;}
    public MyGenericList<Integer> getC(){return Commoners;}
    Scanner in=new Scanner(System.in);
    private String S="";
    public String getS(){return S;}
    public Player[] Select()
    {
      //boolean done=true;
      System.out.println("Welcome to Mafia");
      int N=0,n=5;
      do{
        boolean done=true;
        while(done){
          System.out.println("Enter the number of players: ");
          try{
              N=in.nextInt();
              done=false;
            }
        catch(InputMismatchException inp){
              System.out.println("Wrong input, enter again");
              in.next();
            }
      }
        if(N<6){System.out.println("Number of players should be greater than 6");}
      }while(N<6);
      Player[] Players=new Player[N];   //array to store different types of players
      System.out.println("Choose a character");
      System.out.println("1) Mafia\n2) Detective\n3) Healer\n4) Commoner\n5) Assign Randomly");
      boolean donee=true;
      while(donee){
        try
          {n=in.nextInt();donee=false;}
        catch(InputMismatchException inp)
          {System.out.println("Wrong input, enter again");
          in.next();}}
      System.out.println("You are Player 1.");
      int nom,nod,noh,noc;
      nom=N/5;
      nod=N/5;
      if(N/10==0){noh=1;}
      else{noh=N/10;}
      noc=N-(nom+nod+noh);
      switch(n)
        {
          case 1:System.out.print("You are a Mafia. Other mafias are: ");
              Players[0]=new Mafia(1);
              Mafias.add(0);
                for(int i=0;i<nom-1;i++)    //nom-1 since user is already a mafia
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Mafia(a+1);
                  Mafias.add(a);
                }
                for(int i=0;i<nod;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Detective(a+1);
                  Detectives.add(a);
                }
                for(int i=0;i<noh;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Healer(a+1);
                  Healers.add(a);
                }
                for(int i=0;i<N;i++){if(Players[i]==null){Players[i]=new Commoner(i+1);Commoners.add(i);}}
                for(int i=1;i<Mafias.size();i++){System.out.print("Player"+Integer.toString(Mafias.get(i)+1)+" ");}
                System.out.println("");
                break;
          case 2:System.out.print("You are a Detective. Other detectives are: ");
                Players[0]=new Detective(1);
                Detectives.add(0);
                for(int i=0;i<nom;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Mafia(a+1);
                  Mafias.add(a);
                }
                for(int i=0;i<nod-1;i++)    //nom-1 since user is already a detective
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Detective(a+1);
                  Detectives.add(a);
                }
                for(int i=0;i<noh;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Healer(a+1);
                  Healers.add(a);
                }
                for(int i=0;i<N;i++){if(Players[i]==null){Players[i]=new Commoner(i+1);Commoners.add(i);}}
                for(int i=1;i<Detectives.size();i++){System.out.print("Player"+Integer.toString(Detectives.get(i)+1)+" ");}
                System.out.println("");
                break;
          case 3:System.out.print("You are a Healer. Other healers are: ");
                Players[0]=new Healer(1);
                Healers.add(0);
                for(int i=0;i<nom;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Mafia(a+1);
                  Mafias.add(a);
                }
                for(int i=0;i<nod;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Detective(a+1);
                  Detectives.add(a);
                }
                for(int i=0;i<noh-1;i++)    //nom-1 since user is already a healer
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Healer(a+1);
                  Healers.add(a);
                }
                for(int i=0;i<N;i++){if(Players[i]==null){Players[i]=new Commoner(i+1);Commoners.add(i);}}
                for(int i=1;i<Healers.size();i++){System.out.print("Player"+Integer.toString(Healers.get(i)+1)+" ");}
                System.out.println("");
                break;
          case 4:System.out.print("You are a commoner. ");
                Players[0]=new Commoner(1);
                Commoners.add(0);
                for(int i=0;i<nom;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Mafia(a+1);
                  Mafias.add(a);
                }
                for(int i=0;i<nod;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Detective(a+1);
                  Detectives.add(a);
                }
                for(int i=0;i<noh;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Healer(a+1);
                  Healers.add(a);
                }
                for(int i=1;i<N;i++){if(Players[i]==null){Players[i]=new Commoner(i+1);Commoners.add(i);}}
                break;
          case 5:
                int huh=RANDOM(0,4);
                if(huh==0){Players[0]=new Mafia(1);nom--;System.out.print("You are a Mafia. Other mafias are: ");Mafias.add(0);}
                else if(huh==1){Players[0]=new Detective(1);nod--;System.out.print("You are a detective. Other detectives are: ");Detectives.add(0);}
                else if(huh==2){Players[0]=new Healer(1);noh--;System.out.print("You are a Healer. Other healers are: ");Healers.add(0);}
                else{Players[0]=new Commoner(1);System.out.print("You are a commoner. ");Commoners.add(0);}
                for(int i=0;i<nom;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Mafia(a+1);
                  Mafias.add(a);
                }
                for(int i=0;i<nod;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Detective(a+1);
                  Detectives.add(a);
                }
                for(int i=0;i<noh;i++)
                {
                  int a;
                  do{a=RANDOM(0,N);}while(Players[a]!=null);
                  Players[a]=new Healer(a+1);
                  Healers.add(a);
                }
                for(int i=0;i<N;i++){if(Players[i]==null){Players[i]=new Commoner(i+1);Commoners.add(i);}}
                if(huh==0){for(int i=1;i<Mafias.size();i++){System.out.print("Player"+Integer.toString(Mafias.get(i)+1)+" ");}}
                else if(huh==1){for(int i=1;i<Detectives.size();i++){System.out.print("Player"+Integer.toString(Detectives.get(i)+1)+" ");}}
                else if(huh==2){for(int i=1;i<Healers.size();i++){System.out.print("Player"+Integer.toString(Healers.get(i)+1)+" ");}}
                else{huh=3;}
                System.out.println("");
                break;
        }
        for(int i=0;i<Mafias.size();i++){
          if(Mafias.get(i).equals(0)){S+="Player1[User] ";}
          else{S+="Player"+Integer.toString(Mafias.get(i)+1)+" ";}}
        S+="were Mafias.\n";
        for(int i=0;i<Detectives.size();i++){
          if(Detectives.get(i).equals(0)){S+="Player1[User] ";}
          else{S+="Player"+Integer.toString(Detectives.get(i)+1)+" ";}}
        S+="were Detectives.\n";
        for(int i=0;i<Healers.size();i++){
          if(Healers.get(i).equals(0)){S+="Player1[User] ";}
          else{S+="Player"+Integer.toString(Healers.get(i)+1)+" ";}}
        S+="were Healers.\n";
        for(int i=0;i<Commoners.size();i++){
          if(Commoners.get(i).equals(0)){S+="Player1[User] ";}
          else{S+="Player"+Integer.toString(Commoners.get(i)+1)+" ";}}
        S+="were Commoners.\n";
        //SS+=S;
        return Players;
      }
    }
  public static class Play
  {
    public void play()
    {
      String S="";
      SelectCharacter SC=new SelectCharacter();
      Player[] Players=SC.Select();   //try tostring here
      S+=SC.getS();
      int i=1;
      //int FLAG=-1;    //to determine whether the mafias won or lost the game
      Rounds round=new Rounds();
      do{
        System.out.println("Round "+Integer.toString(i)+":");
        String pl="";
        int activecount=0;
        for(int ii=0;ii<Players.length;ii++){if(Players[ii]!=null){activecount++;pl+="Player"+Integer.toString(ii+1)+" ";}}
        System.out.println(Integer.toString(activecount)+" players are remaining: "+pl+" are alive.");
        Players=round.startNewRound(SC.getM(),SC.getD(),SC.getH(),SC.getC(),Players);
        System.out.println("---End of round "+Integer.toString(i)+"---");
        i++;
        //System.out.println("          FLAG IS "+Integer.toString(FLAG));
      }while(Endgame1(Players));
      int FLAG=Endgame2(Players);
      if(FLAG==0){System.out.println("Game Over.\nThe Mafias have lost.");System.out.println(S);}
      if(FLAG==1){System.out.println("Game Over.\nThe Mafias have won.");System.out.println(S);}
    }
  }
  public static boolean Endgame1(Player[] Players)
  {
    int nom=0,nod=0,noh=0,noc=0;
    for(int i=0;i<Players.length;i++){
      if(Players[i] instanceof Mafia)nom++;
      if(Players[i] instanceof Detective)nod++;
      if(Players[i] instanceof Healer)noh++;
      if(Players[i] instanceof Commoner)noc++;
    }
    if(nom==(nod+noh+noc)){return false;}
    else if(nom==0){return false;}
    else{return true;}       //FLAG -1 means the game is still on
  }
  public static int Endgame2(Player[] Players)
  {
    int nom=0,nod=0,noh=0,noc=0;
    for(int i=0;i<Players.length;i++){
      if(Players[i] instanceof Mafia)nom++;
      if(Players[i] instanceof Detective)nod++;
      if(Players[i] instanceof Healer)noh++;
      if(Players[i] instanceof Commoner)noc++;
    }
    if(nom==(nod+noh+noc)){return 1;}
    else if(nom==0){return 0;}
    else{return-1;}       //FLAG -1 means the game is still on
  }
  public static int RANDOM(int a,int b)
  {
    Random r=new Random();
    return r.nextInt(b)+a;  //returns random int between a to b+a-1
  }
  public static class MyGenericList <T>
  {
        private ArrayList <T> myList;
        public MyGenericList() {
        myList = new ArrayList <T>();
        }
        public void add(T o) {
        myList.add(o);
        }
        public T get(int i) {
        return myList.get(i);
        }
        public int size() {
        return myList.size();
        }
}
  public static void main(String[] args)
  {
    Play p=new Play();
    p.play();
  }
}
