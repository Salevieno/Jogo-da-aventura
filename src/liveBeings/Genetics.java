package liveBeings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import utilities.UtilG;

public class Genetics
{
	private List<Double> genes ;
	private List<List<Double>> geneMods ;
	private int fitness ;
	
	public Genetics()
	{
		genes = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0))  ;
		geneMods = new ArrayList<>() ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;

		randomizeGenes() ;
		genes = normalize(genes) ;
//		randomizeGeneMods() ;
	}
	
	public Genetics(List<Double> genes)
	{
		this.genes = new ArrayList<>(genes)  ;
		geneMods = new ArrayList<>() ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;

//		geneMods.forEach(geneMod -> geneMod.replaceAll(mod -> UtilG.Round(Math.random() - Math.random(), 3)));
	}
	
	public void randomizeGenes()
	{
		genes.replaceAll(gene -> UtilG.Round(Math.random(), 3));
	}
	
	public void randomizeGeneMods()
	{
		geneMods.forEach(geneMod -> geneMod.replaceAll(mod -> Math.random() - Math.random()));
	}
	
	public static List<Double> makePositive(List<Double> list)
	{
		list.replaceAll(value -> Math.max(value, 0)) ;
		return list ;
	}
	
	public List<Double> avr(List<Double> genes1, List<Double> genes2)
	{
		List<Double> avr = new ArrayList<>() ;
		for (int i = 0 ; i <= genes1.size() - 1; i += 1)
		{
			avr.add((genes1.get(i) + genes2.get(i)) / 2.0 ) ;
		}
		
		return avr ;
	}
	
	public static List<Double> normalize(List<Double> list)
	{
		// makes the sum of all genes = 1
		double sum = 0 ;
		for (double value : list)
		{
			sum += value ;
		}
		
		final double finalSum = sum ;
		list.replaceAll(value -> UtilG.Round(value / finalSum, 3));
		
		return list ;
	}
	
 	public List<Double> getModifiedGenes(String playerMove)
	{
		List<Double> modifiedGenes = new ArrayList<>(genes) ;
		double incAtkGene = playerMove.equals("Y") ? geneMods.get(0).get(0) : 0 ;
		double incDefGene = playerMove.equals("U") ? geneMods.get(1).get(1) : 0 ;
		double incSpellGene = playerMove.equals("0") ? geneMods.get(2).get(2) : 0 ;
		
		modifiedGenes.set(0, genes.get(0) + incAtkGene) ;
		modifiedGenes.set(1, genes.get(1) + incDefGene) ;
		modifiedGenes.set(2, genes.get(2) + incSpellGene) ;

		modifiedGenes = makePositive(modifiedGenes) ;
		modifiedGenes = normalize(modifiedGenes) ;
		
//		System.out.println("player move = " + playerMove);
//		System.out.println("modifiedGenes = " + modifiedGenes + " = " + modifiedGenes.stream().mapToDouble(d -> d).sum()) ;
		
		return modifiedGenes ;
	}

	public boolean areSelected(List<Integer> listFitness)
	{
		return Collections.min(listFitness) <= fitness ;
	}	
	
	public void mutate()
	{
		randomizeGenes() ;
		genes = normalize(genes) ;
	}
	
	public void breed(List<List<Double>> bestGenes)
	{
		int i1 = UtilG.randomIntFromTo(0, bestGenes.size() - 1 ) ;
		int i2 = UtilG.randomIntFromTo(0, bestGenes.size() - 1 ) ;
		
		if (UtilG.chance(0.1))
		{
			mutate() ;
			return ;
		}
		System.out.println(i1 + " " + i2);
		System.out.println(bestGenes.get(i1) + " " + bestGenes.get(i2));
		System.out.println(avr(bestGenes.get(i1), bestGenes.get(i2)));
		System.out.println();

		genes = new ArrayList<>(avr(bestGenes.get(i1), bestGenes.get(i2))) ;
	}
	
	public void updateFitness(int opponentEndLife, int opponentMaxLife, int endLife, int maxLife)
	{
		int fitness = Genetics.calcFitness(opponentEndLife, opponentMaxLife, endLife, maxLife) ;
		setFitness(fitness) ;
	}
	
	public static int calcFitness(int opponentEndLife, int opponentMaxLife, int endLife, int maxLife)
	{
		int opponentLifeScore = (int) (1000 * opponentEndLife / (double) opponentMaxLife) ;
		int lifeScore = (int) (1000 * endLife / (double) maxLife) ;
		return 1000 - opponentLifeScore + lifeScore ;
	}
	
	public List<Double> getGenes()
	{
		return genes;
	}

	public void setGenes(List<Double> genes)
	{
		this.genes = genes;
	}

	public List<List<Double>> getGeneMods()
	{
		return geneMods;
	}

	public void setGeneMods(List<List<Double>> geneMods)
	{
		this.geneMods = geneMods;
	}

	public int getFitness()
	{
		return fitness;
	}

	public void setFitness(int fitness)
	{
		this.fitness = fitness;
	}

	@Override
	public String toString()
	{
		return "Genes [genes=" + genes + ", fitness=" + fitness + "]";
	}
	
}
