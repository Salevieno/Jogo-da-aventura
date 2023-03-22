package items;

public enum EquipTypes
{
	sword,
	staff,
	bow,
	claws,
	dagger,
	shield,
	armor,
	emblem;

	private EquipTypes()
	{
		
	}


	public static EquipTypes[] weapons() { return new EquipTypes[] {sword, staff, bow, claws, dagger} ;}

}