import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as userActions from '../../../store/user/userSlice';
import LoginForm from '../../../components/login/LoginForm';

jest.mock('react-redux');

describe('login form', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
  });

  test('login form', () => {
    render(<LoginForm status="idle" error="" />);

    expect(screen.getByPlaceholderText('Login')).toHaveFocus();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
    expect(screen.getByRole('button', {name: 'Login'})).toBeDisabled();
  });

  test('submit empty fields', async () => {
    render(<LoginForm status="idle" error="" />);

    const loginButton = screen.getByRole('button', {name: 'Login'});
    fireEvent.submit(loginButton);

    expect(await screen.findAllByText("The field can't be empty")).toHaveLength(
      2
    );
  });

  test('email error message', async () => {
    const errorMessage = 'Invalid email address';

    render(<LoginForm status="idle" error="" />);

    const loginButton = screen.getByRole('button', {name: 'Login'});
    const emailInput = screen.getByPlaceholderText('Login');

    userEvent.type(emailInput, 'qwe');
    fireEvent.submit(loginButton);
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(emailInput, 'qwe@gmail');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(emailInput, '.com');
    await waitFor(() => expect(screen.queryByText(errorMessage)).toBeNull());
  });

  test('login form submit', async () => {
    const loadUser = jest.spyOn(userActions, 'loadUser');

    render(<LoginForm status="idle" error="" />);

    const loginInput = screen.getByPlaceholderText('Login');
    const passwordInput = screen.getByPlaceholderText('Password');
    const loginButton = screen.getByRole('button', {name: 'Login'});

    userEvent.type(loginInput, 'cat@gmail.com');
    userEvent.type(passwordInput, 'asQW12');

    await waitFor(() => expect(loginButton).toBeEnabled());
    expect(dispatch).not.toHaveBeenCalled();

    userEvent.click(loginButton);
    await waitFor(() => expect(dispatch).toHaveBeenCalled());
    expect(loadUser).toHaveBeenCalledWith({
      email: 'cat@gmail.com',
      password: 'asQW12',
    });

    await waitFor(() => expect(loginInput).toHaveDisplayValue(''));
    expect(passwordInput).toHaveDisplayValue('');
  });
});
