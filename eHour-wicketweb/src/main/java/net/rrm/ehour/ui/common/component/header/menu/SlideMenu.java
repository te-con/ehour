package net.rrm.ehour.ui.common.component.header.menu;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

public class SlideMenu extends Panel {
    private static final long serialVersionUID = 6026029033263227314L;

    public SlideMenu(String id, List<MenuItem> menuItemList) {
        super(id);

        Menu menu = new Menu("slideMenu", menuItemList);
        add(menu);
    }

    @SuppressWarnings("serial")
    private class Menu extends Panel {
        private static final String ID_CONTENT = "content";
        private static final String ID_SUBMENU_LIST_CONTAINER = "submenuListContainer";

        public Menu(String id, List<MenuItem> menuItemList) {
            super(id);

            if (!CollectionUtils.isEmpty(menuItemList)) {
                ListView<?> menu = buildMultiLevelMenu("menuList", menuItemList);
                menu.setReuseItems(true);
                add(menu);
            }
        }

        private ListView<MenuItem> buildMultiLevelMenu(String id, List<MenuItem> menuItemList) {
            return new ListView<MenuItem>(id, menuItemList) {
                public void populateItem(final ListItem<MenuItem> item) {
                    final MenuItem menuItem = item.getModelObject();

                    // link or text
                    item.add(menuItem.isLink() ? createLinkFragment(menuItem) : createTextOnlyFragment(menuItem));

                    // sub menus
                    item.add(createSubMenus(menuItem));

                    item.setVisible(menuItem.isVisibleForLoggedInUser());
                }

                private WebMarkupContainer createSubMenus(final MenuItem menuItem) {
                    List<MenuItem> submenuItemList = menuItem.getSubMenus();

                    return CollectionUtils.isEmpty(submenuItemList)
                            ? new WebMarkupContainer(ID_SUBMENU_LIST_CONTAINER)
                            : new Menu(ID_SUBMENU_LIST_CONTAINER, submenuItemList);
                }

                private Fragment createLinkFragment(MenuItem menuItem) {
                    Fragment linkFragment = new Fragment(ID_CONTENT, "link", Menu.this);

                    Link<MenuItem> link = new Link<MenuItem>("menuLink", new Model<MenuItem>(menuItem)) {
                        @Override
                        public void onClick() {
                            MenuItem object = getModelObject();

                            if (object != null) {
                                if (object.getPageParameters() != null) {
                                    setResponsePage(object.getResponsePageClass(), object.getPageParameters());
                                } else {
                                    setResponsePage(object.getResponsePageClass());
                                }

                            }
                        }
                    };

                    linkFragment.add(link);

                    Label linkText = new Label("menuLinkText", new ResourceModel(menuItem.getTitleId()));
                    link.add(linkText);
                    return linkFragment;
                }

                private Fragment createTextOnlyFragment(MenuItem menuItem) {
                    Fragment linkFragment = new Fragment(ID_CONTENT, "textOnly", Menu.this);

                    Label linkText = new Label("menuText", new ResourceModel(menuItem.getTitleId()));

                    linkFragment.add(linkText);
                    return linkFragment;
                }
            };
        }
    }
}
