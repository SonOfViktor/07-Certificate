import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import DrawerMenu from '../../../../components/header/menu/DrawerMenu';
import {useQueryParams} from '../../../../components/header/hooks/useQueryParams';

jest.mock('react-redux');
jest.mock('react-router-dom');
jest.mock('../../../../components/header/hooks/useQueryParams');

describe('drawer menu', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseLocation = jest.spyOn(routerHooks, 'useLocation');
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseLocation.mockReturnValue({pathname: '/'});
    useQueryParams.mockReturnValue([jest.fn(), jest.fn()]);
  });

  test('drawer menu render when user is admin', () => {
    mockedUseSelector
      .mockReturnValueOnce({role: 'admin'})
      .mockReturnValueOnce({role: 'admin'})
      .mockReturnValueOnce({field: 'createDate', order: 'ASC'})
      .mockReturnValue([]);

    render(<DrawerMenu />);

    const menuButton = screen.getByTestId('MenuIcon');

    expect(screen.queryByText('Filter')).toBeNull();

    userEvent.click(menuButton);
    expect(screen.getByText('Filter')).toBeInTheDocument();
    expect(screen.getByText('Admin')).toBeInTheDocument();
  });

  test('drawer menu render when user is not admin', () => {
    mockedUseSelector
      .mockReturnValueOnce({role: 'user'})
      .mockReturnValueOnce({role: 'user'})
      .mockReturnValueOnce({field: 'createDate', order: 'ASC'})
      .mockReturnValue([]);

    render(<DrawerMenu />);

    const menuButton = screen.getByTestId('MenuIcon');

    expect(screen.queryByText('Filter')).toBeNull();

    userEvent.click(menuButton);
    expect(screen.getByText('Filter')).toBeInTheDocument();
    expect(screen.queryByText('Admin')).toBeNull();
  });
});
