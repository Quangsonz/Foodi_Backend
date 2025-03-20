package G9.Foodi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import G9.Foodi.model.Menu;
import G9.Foodi.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;

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
    public void deleteMenuItem(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu item not found with id: " + id);
        }
        menuRepository.deleteById(id);
    }

    @Override
    public Menu getMenuItemById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found with id: " + id));
    }

    @Override
    public Menu updateMenuItem(Long id, Menu menuDetails) {
        Menu menu = getMenuItemById(id);
        
        menu.setName(menuDetails.getName());
        menu.setRecipe(menuDetails.getRecipe());
        menu.setImage(menuDetails.getImage());
        menu.setCategory(menuDetails.getCategory());
        menu.setPrice(menuDetails.getPrice());
        
        return menuRepository.save(menu);
    }
} 