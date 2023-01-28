import {Stack} from '@mui/system';
import Search from './Search';
import NavLinks from './NavLinks';
import Logo from './Logo';
import DrawerMenu from './menu/DrawerMenu';
import {useSearchParams} from 'react-router-dom';
import {useDispatch} from 'react-redux';
import {useEffect} from 'react';
import {setFilter} from '../../store/filter/filterSlice';

const NavContainer = ({children}) => (
  <Stack
    component="nav"
    direction="row"
    justifyContent="space-between"
    alignItems="center"
    flexWrap="wrap"
    padding="10px 0">
    {children}
  </Stack>
);

const NavBar = () => {
  const [searchParams] = useSearchParams();
  const dispatch = useDispatch();

  useEffect(() => {
    const query = Object.fromEntries(searchParams);

    dispatch(setFilter(query));
  }, [searchParams, dispatch]);

  return (
    <NavContainer>
      <Stack direction="row" alignItems="center">
        <DrawerMenu />
        <Logo />
      </Stack>
      <Search />
      <NavLinks />
    </NavContainer>
  );
};

export default NavBar;
