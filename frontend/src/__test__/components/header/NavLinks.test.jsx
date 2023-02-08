import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import * as userActions from '../../../store/user/userSlice';
import NavLinks from '../../../components/header/NavLinks';

jest.mock('../../../components/header/hooks/useQueryParams');
jest.mock('react-redux');
jest.mock('react-router-dom');

describe('navbar links', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseNavigate = jest.spyOn(routerHooks, 'useNavigate');
  const dispatch = jest.fn();
  const navigate = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseNavigate.mockReturnValue(navigate);
    mockedUseSelector.mockReturnValue(2);
  });

  test('navbar render without user', () => {
    mockedUseSelector.mockReturnValueOnce(null);

    render(<NavLinks />);

    expect(screen.getByTestId('FavoriteIcon')).toBeInTheDocument();
    expect(screen.getByTestId('ShoppingCartIcon')).toBeInTheDocument();
    expect(screen.getAllByText('2')).toHaveLength(2);
    expect(screen.getByTestId('LoginIcon')).toBeInTheDocument();
    expect(screen.queryByTestId('LogoutIcon')).toBeNull();
  });

  test('navbar render with user', () => {
    mockedUseSelector.mockReturnValueOnce({firstName: 'Maks', lastName: 'Sil'});

    render(<NavLinks />);

    expect(screen.getByTestId('FavoriteIcon')).toBeInTheDocument();
    expect(screen.getByTestId('ShoppingCartIcon')).toBeInTheDocument();
    expect(screen.getAllByText('2')).toHaveLength(2);
    expect(screen.getByText('MS')).toBeInTheDocument();
    expect(screen.getByTestId('LogoutIcon')).toBeInTheDocument();
    expect(screen.queryByTestId('LoginIcon')).toBeNull();
  });

  test('click favorite button', () => {
    mockedUseSelector.mockReturnValueOnce(null);

    render(<NavLinks />);

    const favoriteButton = screen.getByTestId('FavoriteIcon');
    userEvent.click(favoriteButton);

    expect(navigate).toHaveBeenCalledWith('/favorite');
  });

  test('click checkout button', () => {
    mockedUseSelector.mockReturnValueOnce(null);

    render(<NavLinks />);

    const checkoutButton = screen.getByTestId('ShoppingCartIcon');
    userEvent.click(checkoutButton);

    expect(navigate).toHaveBeenCalledWith('/checkout');
  });

  test('click login button', () => {
    mockedUseSelector.mockReturnValueOnce(null);

    render(<NavLinks />);

    const loginButton = screen.getByTestId('LoginIcon');
    userEvent.click(loginButton);

    expect(navigate).toHaveBeenCalledWith('/login');
  });

  test('click user button', () => {
    mockedUseSelector.mockReturnValueOnce({firstName: 'Maks', lastName: 'Sil'});

    render(<NavLinks />);

    const userButton = screen.getByText('MS');
    userEvent.click(userButton);

    expect(navigate).toHaveBeenCalledWith('/user');
  });

  test('click logout button', () => {
    mockedUseSelector.mockReturnValueOnce({firstName: 'Maks', lastName: 'Sil'});
    const logoutAction = jest.spyOn(userActions, 'logout');

    render(<NavLinks />);

    const logoutButton = screen.getByTestId('LogoutIcon');
    userEvent.click(logoutButton);

    expect(dispatch).toHaveBeenCalled();
    expect(logoutAction).toHaveBeenCalled();
  });
});
