package liveBeings;

import java.util.Arrays;

public class AttackModifiers
{
	private double[] atkMod ;
	private double[] defMod ;
	private double[] dexMod ;
	private double[] agiMod ;
	private double[] atkCritMod ;
	private double[] defCritMod ;
	private double[] stunMod ;
	private double[] blockMod ;
	private double[] bloodMod ;
	private double[] poisonMod ;
	private double[] silenceMod ;

	public AttackModifiers()
	{
		this.atkMod = new double[2] ;
		this.defMod = new double[2] ;
		this.dexMod = new double[2] ;
		this.agiMod = new double[2] ;
		this.atkCritMod = new double[1] ;
		this.defCritMod = new double[1] ;
		this.stunMod = new double[3] ;
		this.blockMod = new double[3] ;
		this.bloodMod = new double[3] ;
		this.poisonMod = new double[3] ;
		this.silenceMod = new double[3] ;
	}
	
	public AttackModifiers(double[] stunMod, double[] blockMod, double[] bloodMod, double[] poisonMod, double[] silenceMod)
	{
		this.atkMod = new double[2] ;
		this.defMod = new double[2] ;
		this.dexMod = new double[2] ;
		this.agiMod = new double[2] ;
		this.atkCritMod = new double[1] ;
		this.defCritMod = new double[1] ;
		this.stunMod = stunMod;
		this.blockMod = blockMod;
		this.bloodMod = bloodMod;
		this.poisonMod = poisonMod;
		this.silenceMod = silenceMod;
	}
	
	public AttackModifiers(double[] atkMod, double[] defMod, double[] dexMod, double[] agiMod, double[] atkCritMod,
			double[] defCritMod, double[] stunMod, double[] blockMod, double[] bloodMod, double[] poisonMod,
			double[] silenceMod)
	{
		this.atkMod = atkMod;
		this.defMod = defMod;
		this.dexMod = dexMod;
		this.agiMod = agiMod;
		this.atkCritMod = atkCritMod;
		this.defCritMod = defCritMod;
		this.stunMod = stunMod;
		this.blockMod = blockMod;
		this.bloodMod = bloodMod;
		this.poisonMod = poisonMod;
		this.silenceMod = silenceMod;
	}

	public double[] getAtkMod() {
		return atkMod;
	}

	public double[] getDefMod() {
		return defMod;
	}

	public double[] getDexMod() {
		return dexMod;
	}

	public double[] getAgiMod() {
		return agiMod;
	}

	public double[] getAtkCritMod() {
		return atkCritMod;
	}

	public double[] getDefCritMod() {
		return defCritMod;
	}

	public double[] getStunMod() {
		return stunMod;
	}

	public double[] getBlockMod() {
		return blockMod;
	}

	public double[] getBloodMod() {
		return bloodMod;
	}

	public double[] getPoisonMod() {
		return poisonMod;
	}

	public double[] getSilenceMod() {
		return silenceMod;
	}

	@Override
	public String toString() {
		return "AttackModifiers [stunMod=" + Arrays.toString(stunMod) + ", blockMod=" + Arrays.toString(blockMod)
				+ ", bloodMod=" + Arrays.toString(bloodMod) + ", poisonMod=" + Arrays.toString(poisonMod)
				+ ", silenceMod=" + Arrays.toString(silenceMod) + "]";
	}
	
}
