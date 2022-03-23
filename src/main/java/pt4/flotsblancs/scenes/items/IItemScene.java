package pt4.flotsblancs.scenes.items;

import pt4.flotsblancs.router.IScene;

public interface IItemScene<I extends Item> extends IScene {
    public void selectItem(I item);
}
