import MenuIcon from '@mui/icons-material/Menu';
import {Drawer, List} from '@mui/material';
import {useState} from 'react';
import {useSelector} from 'react-redux';
import {selectUser} from '../../../store/user/userSelectors';
import HoverIconButton from '../../ui/HoverIconButton';
import AdminLink from './AdminLink';
import CloseMenuButton from './CloseMenuButton';
import Filter from './Filter';
import Sorting from './Sorting';

const DrawerMenu = () => {
  const [isOpen, setIsOpen] = useState(false);
  const user = useSelector(selectUser);

  const toggleDrawerMenu = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <HoverIconButton onClick={toggleDrawerMenu}>
        <MenuIcon />
      </HoverIconButton>
      <Drawer anchor="left" open={isOpen} onClose={toggleDrawerMenu}>
        <List sx={{width: 250}}>
          <Sorting />
          <Filter />
          {user?.role === 'admin' && <AdminLink onClose={toggleDrawerMenu} />}
          <CloseMenuButton onClose={toggleDrawerMenu} />
        </List>
      </Drawer>
    </>
  );
};

export default DrawerMenu;
