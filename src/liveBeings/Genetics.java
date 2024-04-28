package liveBeings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import libUtil.Util;

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
		randomizeGeneMods() ;
	}
	
	public Genetics(List<Double> genes)
	{
		this.genes = new ArrayList<>(genes)  ;
		geneMods = new ArrayList<>() ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0, 0.0)) ;

		randomizeGeneMods() ;
	}
	
	public Genetics(List<Double> genes, List<List<Double>> geneMods)
	{
		this.genes = new ArrayList<>(genes)  ;
		this.geneMods = new ArrayList<>(geneMods)  ;
	}
	
	public void randomizeGenes()
	{
		genes.replaceAll(gene -> Util.Round(Math.random(), 3));
		genes = normalize(genes) ;
	}
	
	public void randomizeGeneMods()
	{
		do
		{
			geneMods.forEach(geneMod -> geneMod.replaceAll(mod -> Math.random() - Math.random()));
			geneMods.replaceAll(geneMod -> normalize(geneMod)) ;
		} while (!geneModsOk()) ;
		
		geneMods.forEach(geneMod -> geneMod.replaceAll(mod -> Util.Round(mod, 3))) ;
		
	}
	
	private boolean geneModsOk()
	{
		
		for (int i = 0 ; i <= genes.size() - 1 ; i += 1)
		{
			for (List<Double> geneMod : geneMods)
			{
				if (genes.get(i) + geneMod.get(i) < 0)
				{
					return false ;
				}
				if ( 1 < genes.get(i) + geneMod.get(i))
				{
					return false ;
				}				
			}
		}
		
		return true ;
		
	}
	
	public static List<Double> makePositive(List<Double> list)
	{
		list = new ArrayList<>(list) ;
		list.replaceAll(value -> Math.max(value, 0)) ;
		return list ;
	}
	
	public List<Double> avr(List<Double> genes1, List<Double> genes2)
	{
		List<Double> avr = new ArrayList<>() ;
		for (int i = 0 ; i <= genes1.size() - 1; i += 1)
		{
			avr.add(Util.Round((genes1.get(i) + genes2.get(i)) / 2.0, 3) ) ;
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
		list.replaceAll(value -> Util.Round(value / finalSum, 3));
		
		return list ;
	}
	
 	public List<Double> getModifiedGenes(String playerMove)
	{
		List<Double> modifiedGenes = new ArrayList<>(genes) ;
		
		if (playerMove == null) { return modifiedGenes ;}
		
		double incAtkGene = playerMove.equals("Y") ? geneMods.get(0).get(0) : 0 ;
		double incDefGene = playerMove.equals("U") ? geneMods.get(1).get(1) : 0 ;
		double incSpellGene = playerMove.equals("0") ? geneMods.get(2).get(2) : 0 ;
		
		modifiedGenes.set(0, genes.get(0) + incAtkGene) ;
		modifiedGenes.set(1, genes.get(1) + incDefGene) ;
		modifiedGenes.set(2, genes.get(2) + incSpellGene) ;

//		modifiedGenes = makePositive(modifiedGenes) ;
		modifiedGenes = normalize(modifiedGenes) ;
//		System.out.println("modified genes = " + modifiedGenes) ;
		
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
		randomizeGeneMods() ;
	}
	
	public void breed(List<List<Double>> bestGenes, int totalParentFitness)
	{
		int i1 = Util.randomIntFromTo(0, bestGenes.size() - 1 ) ;
		int i2 = Util.randomIntFromTo(0, bestGenes.size() - 1 ) ;

		double mutationChance = 0.2 * (1 - totalParentFitness / (6 * 2000)) ;
		
		if (Util.chance(mutationChance))
		{
			mutate() ;
			return ;
		}

		genes = new ArrayList<>(avr(bestGenes.get(i1), bestGenes.get(i2))) ;
	}
	
	public void breed2(List<Genetics> bestGenes, int totalParentFitness)
	{
		int i1 = Util.randomIntFromTo(0, bestGenes.size() - 1 ) ;
		int i2 = Util.randomIntFromTo(0, bestGenes.size() - 1 ) ;
		
		double mutationChance = 0.2 * (1 - totalParentFitness / (6 * 2000)) ;
		
		if (Util.chance(mutationChance))
		{
			mutate() ;
			return ;
		}
		
		for (int i = 0 ; i <= genes.size() - 1 ; i += 1)
		{
			boolean getFromParent1 = Util.chance(0.5) ;
			genes.set(i, getFromParent1 ? bestGenes.get(i1).getGenes().get(i) : bestGenes.get(i2).getGenes().get(i)) ;
			geneMods.set(i, getFromParent1 ? bestGenes.get(i1).getGeneMods().get(i) : bestGenes.get(i2).getGeneMods().get(i)) ;
		}
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
