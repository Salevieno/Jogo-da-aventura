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

	public static EquipTypes[] weapons() { return new EquipTypes[] {sword, staff, bow, claws, dagger} ;}

}