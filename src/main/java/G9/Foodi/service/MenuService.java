package G9.Foodi.service;

import java.util.List;

import G9.Foodi.model.Menu;

public interface MenuService {
    List<Menu> getAllMenuItems();
    Menu createMenuItem(Menu menu);
    void deleteMenuItem(String id);
    Menu getMenuItemById(String id);
    Menu updateMenuItem(String id, Menu menuDetails);
}