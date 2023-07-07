package liveBeings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Genes
{
	private List<Double> genes ;
	private List<List<Double>> geneMods ;
	private int fitness ;
	
	public Genes()
	{
		genes = new ArrayList<>(Arrays.asList(0.14606542271035106, 0.11939312567256516))  ;
		geneMods = new ArrayList<>() ;
		geneMods.add(Arrays.asList(0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0)) ;

		geneMods.forEach(geneMod -> geneMod.replaceAll(mod -> Math.random() - Math.random()));
	}
	
	public Genes(List<Double> genes)
	{
		this.genes = genes  ;
		geneMods = new ArrayList<>() ;
		geneMods.add(Arrays.asList(0.0, 0.0)) ;
		geneMods.add(Arrays.asList(0.0, 0.0)) ;

		geneMods.forEach(geneMod -> geneMod.replaceAll(mod -> Math.random() - Math.random()));
	}
	
	public void randomize()
	{
		genes.replaceAll(gene -> Math.random());
	}
	
	public List<Double> getModifiedGenes(String playerMove)
	{
		List<Double> modifiedGenes = new ArrayList<>(genes) ;
		double incAtkGene = playerMove.equals("Y") ? geneMods.get(0).get(0) : 0 ;
		double incDefGene = playerMove.equals("U") ? geneMods.get(1).get(1) : 0 ;
		
		modifiedGenes.set(0, genes.get(0) + incAtkGene) ;
		modifiedGenes.set(1, genes.get(1) + incDefGene) ;
		
		System.out.println("player move = " + playerMove);
		System.out.println("modifiedGenes = " + modifiedGenes);
		
		return modifiedGenes ;
	}

	public boolean areSelected(List<Integer> listFitness)
	{
		return Collections.min(listFitness) <= fitness ;
	}	
	
	public List<Double> breed(List<List<Double>> bestGenes)
	{
		List<Double> newGenes = new ArrayList<>(genes) ;
		for (int i = 0 ; i <= newGenes.size() - 1; i += 1)
		{
			newGenes.set(i, bestGenes.get(i).stream().mapToDouble(d -> d).average().getAsDouble()) ;
		}
		
		return newGenes ;
	}
	
	public static int calcFitness(int opponentEndLife, int opponentMaxLife, int endLife, int maxLife)
	{
		int opponentLifeScore = (int) (100 * opponentEndLife / (double) opponentMaxLife) ;
		int lifeScore = (int) (100 * endLife / (double) maxLife) ;
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

	@Override
	public String toString()
	{
		return "Genes [genes=" + genes + ", fitness=" + fitness + "]";
	}
	
	
}
