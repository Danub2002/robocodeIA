package pkgRobots;

import org.jgap.*;
import org.jgap.impl.*;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

@SuppressWarnings("serial")

public class robocodeGA extends FitnessFunction {
	// set amount of generations to evolve
	public static final int MAX_GENERATIONS = 25; 
	// set population size per generation
	public static final int POPULATION_SIZE = 40; 
	// amount of chromosomes
	public static final int CHROMOSOME_AMOUNT = 6; 
	
	static int robotScore,enemyScore;
	
	
	// number of rounds
	public static final int NUMBER_OF_ROUNDS = 3;
	
	public void run() throws Exception{
		Configuration conf = new  DefaultConfiguration();
		conf.addGeneticOperator(new MutationOperator(conf,20)); // add new crossover opp 1/10% 
		conf.setPreservFittestIndividual(true); // use elitsim
		conf.setFitnessFunction(this);
		
		// set up sample genes
		Gene[] sampleGenes = new Gene[CHROMOSOME_AMOUNT];
		sampleGenes[0] = new DoubleGene(conf,100,200); // distance
		sampleGenes[1] = new DoubleGene(conf,100,350); // safe distance from the wall
		
		sampleGenes[2] = new DoubleGene(conf,0,100); // Energy threshold 
		sampleGenes[3] = new DoubleGene(conf,10,40); // max Stationary time
		
		sampleGenes[4] = new DoubleGene(conf,100,250); // set back after hitting the wall
		sampleGenes[5] = new DoubleGene(conf,10,90); // Angle
		IChromosome sampleChromosome = new Chromosome(conf,sampleGenes);
		conf.setSampleChromosome(sampleChromosome);
		conf.setPopulationSize(POPULATION_SIZE);
		
		Genotype population = Genotype.randomInitialGenotype(conf);
		IChromosome fittestSolution = null;
		
		//evolve population
		for ( int gen = 0; gen<MAX_GENERATIONS; gen++ ) {
			System.out.println("evolving");
			population.evolve(); // evolve population
			System.out.println("after evolve");
			fittestSolution = population.getFittestChromosome(); // find fittest of population
			System.out.printf("\nafter %d generations the best solution is %s \n",gen + 1,fittestSolution);	
		}  	

		buildRobot(fittestSolution); //pass best solution to build
		System.exit(0); // clean exit
	}
	
	public static void main(String[] args) throws Exception {
		new robocodeGA().run(); // run main
	}
	
	public boolean battleResults(String name,int score){
		String same = "pkgRobots.BigGA*"; // enter robot name here with folder prefix
		
		//get results of battle
		if(name.equals(same)){
			robotScore = score;
			return true;
		}
		else {
			enemyScore = score;
			return false;
		}
	}
	
	private void buildRobot (IChromosome chromosome) {
		int i = 0;
		
		//break down chromosome to array
		double[ ] chromo = new double[ CHROMOSOME_AMOUNT ];
		for ( Gene gene : chromosome.getGenes() ) {
			chromo[i] += (Double) gene.getAllele(); // get value from gene
			i++;
		}
		createRobot.create(chromo);	 // create robot - func in createRobot.java
	}
	
	@Override
    protected double evaluate( IChromosome chromosome) {
    	int fitness,
    		numberOfRounds = NUMBER_OF_ROUNDS;
    	
    	buildRobot(chromosome); // build robot
	    
        RobocodeEngine engine = new RobocodeEngine(); // create robocode engine
        engine.addBattleListener(new battleObserver()); // add battle listener to engine
        engine.setVisible(false); // show battle in GUI ?
        
        BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // battle field size
        RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Walls,sample.Crazy,pkgRobots.BigGA*"); // which sample bots to take to battle
        BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
        
        engine.runBattle(battleSpec, true); // run battle - wait till the battle is over
        engine.close(); // clean up engine
        
        fitness = robotScore; // set fitness score
        
        return fitness > 0 ? fitness : 0; // return fitness score if it's over 0
    }
    
    public void sortScore(int roboScore,int enemScore){
    	robotScore = roboScore;
    	enemyScore = enemScore;
    }
	
}
