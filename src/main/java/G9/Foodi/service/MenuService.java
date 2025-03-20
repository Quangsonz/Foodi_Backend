package G9.Foodi.service;

import java.util.List;

import G9.Foodi.model.Menu;

public interface MenuService {
    List<Menu> getAllMenuItems();
    Menu createMenuItem(Menu menu);
    void deleteMenuItem(Long id);
    Menu getMenuItemById(Long id);
    Menu updateMenuItem(Long id, Menu menuDetails);
} 