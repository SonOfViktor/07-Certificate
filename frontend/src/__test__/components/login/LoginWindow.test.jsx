import {render, screen} from '@testing-library/react';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import * as userActions from '../../../store/user/userSlice';
import LoginWindow from '../../../components/login/LoginWindow';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('login form', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseLocation = jest.spyOn(routerHooks, 'useLocation');
  const mockedUseNavigate = jest.spyOn(routerHooks, 'useNavigate');

  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseLocation.mockReturnValue({});
  });

  test('user status created', () => {
    const cleanUserStatus = jest.spyOn(userActions, 'cleanUserStatus');
    mockedUseSelector.mockReturnValue({
      status: 'created',
      error: '',
      user: null,
    });

    render(<LoginWindow />);

    expect(cleanUserStatus).toHaveBeenCalled();
    expect(
      screen.getByText(/user was created successfully/i)
    ).toBeInTheDocument();
  });

  test('redirect if user is exist', () => {
    const navigate = jest.fn();
    mockedUseSelector.mockReturnValue({
      status: 'idle',
      error: '',
      user: {},
    });
    mockedUseNavigate.mockReturnValue(navigate);

    render(<LoginWindow />);

    expect(navigate).toHaveBeenCalledWith('/');
  });
});
