package G9.Foodi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import G9.Foodi.model.Menu;
import G9.Foodi.repository.MenuRepository;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> getAllMenuItems() {
        return menuRepository.findAll();
    }

    @Override
    public Menu createMenuItem(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenuItem(String id) {
        if (!menuRepository.existsById(id)) {
            throw new IllegalArgumentException("Menu item not found with id: " + id);
        }
        menuRepository.deleteById(id);
    }

    @Override
    public Menu getMenuItemById(String id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with id: " + id));
    }

    @Override
    public Menu updateMenuItem(String id, Menu menuDetails) {
        Menu menu = getMenuItemById(id);
        
        menu.setName(menuDetails.getName());
        menu.setRecipe(menuDetails.getRecipe());
        menu.setImage(menuDetails.getImage());
        menu.setCategory(menuDetails.getCategory());
        menu.setPrice(menuDetails.getPrice());
        
        return menuRepository.save(menu);
    }
}