import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import * as userActions from '../../../store/user/userSlice';
import RegisterForm from '../../../components/register/RegisterForm';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('coupon list component', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseNavigate = jest.spyOn(routerHooks, 'useNavigate');

  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
  });

  test('register form render', () => {
    render(<RegisterForm />);

    expect(screen.getByLabelText('Login Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Login Name')).toHaveFocus();
    expect(screen.getByLabelText('First Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Last Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Password')).toBeInTheDocument();
    expect(screen.getByLabelText('Repeat Password')).toBeInTheDocument();
  });

  test('submit form with empty fields', async () => {
    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});

    fireEvent.submit(confirmButton);

    expect(await screen.findAllByText(/can't be empty/)).toHaveLength(5);
    expect(dispatch).not.toHaveBeenCalled();
  });

  test('email error messages', async () => {
    const errorMessage = /email .* more than 30 .*/i;

    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});
    const emailInput = screen.getByLabelText('Login Name');

    userEvent.type(emailInput, 'qwe');
    fireEvent.submit(confirmButton);
    expect(
      await screen.findByText('Invalid email address')
    ).toBeInTheDocument();

    userEvent.type(emailInput, 'qwe@rtt');
    expect(
      await screen.findByText('Invalid email address')
    ).toBeInTheDocument();

    userEvent.clear(emailInput);
    userEvent.type(emailInput, 'qwadsfasdfsadfasdfadfsadfae@rtt.ru');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('first name error messages', async () => {
    const errorMessage = /first name .* 2 - 20 .* letters/i;

    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});
    const firstNameInput = screen.getByLabelText('First Name');

    userEvent.type(firstNameInput, 'q');
    fireEvent.submit(confirmButton);

    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(firstNameInput, 'qad3dfg');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.clear(firstNameInput);
    userEvent.type(firstNameInput, 'qwertasdfgzxcvbyiophd');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('last name error messages', async () => {
    const errorMessage = /last name .* 2 - 20 .* letters/i;

    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});
    const lastNameInput = screen.getByLabelText('Last Name');

    userEvent.type(lastNameInput, 'q');
    fireEvent.submit(confirmButton);

    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(lastNameInput, 'qad3dfg');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.clear(lastNameInput);
    userEvent.type(lastNameInput, 'qwertasdfgzxcvbyiophd');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('password error messages', async () => {
    const errorMessage =
      /password .* 4 - 30 .* digits, upper and lower case letters/i;

    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});
    const passwordInput = screen.getByLabelText('Password');

    userEvent.type(passwordInput, 'q');
    fireEvent.submit(confirmButton);

    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(passwordInput, '2W');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(passwordInput, 'QWERTasdfg12345VCXAS23456asdfg');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.clear(passwordInput);
    userEvent.type(passwordInput, 'QWERTasdfg');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.clear(passwordInput);
    userEvent.type(passwordInput, '12345asdfg');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.clear(passwordInput);
    userEvent.type(passwordInput, 'QWERT12345');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('repeat password error messages', async () => {
    const errorMessage =
      /repeat password .* 4 - 30 .* digits, upper and lower case letters/i;

    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});
    const repeatPasswordInput = screen.getByLabelText('Repeat Password');

    userEvent.type(repeatPasswordInput, 'q');
    fireEvent.submit(confirmButton);
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(repeatPasswordInput, '2W');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();

    userEvent.type(repeatPasswordInput, 'QWERTasdfg12345VCXAS23456asdfg');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('password and repeat password match', async () => {
    const errorMessage = /password and repeat password must match/i;

    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});
    const passwordInput = screen.getByLabelText('Password');
    const repeatPasswordInput = screen.getByLabelText('Repeat Password');

    userEvent.type(passwordInput, 'asQW12');
    userEvent.type(repeatPasswordInput, 'asQW12');
    fireEvent.submit(confirmButton);
    await waitFor(() => expect(screen.queryByText(errorMessage)).toBeNull());

    userEvent.type(repeatPasswordInput, '2');
    expect(await screen.findByText(errorMessage)).toBeInTheDocument();
  });

  test('submit valid form', async () => {
    const createUserAction = jest.spyOn(userActions, 'createUser');

    render(<RegisterForm />);

    const confirmButton = screen.getByRole('button', {name: 'Sign up'});
    userEvent.type(screen.getByLabelText('Login Name'), 'uliana@gmail.com');
    userEvent.type(screen.getByLabelText('First Name'), 'Uliana');
    userEvent.type(screen.getByLabelText('Last Name'), 'Maksovna');
    userEvent.type(screen.getByLabelText('Password'), 'asQW12');
    userEvent.type(screen.getByLabelText('Repeat Password'), 'asQW12');

    fireEvent.submit(confirmButton);

    await waitFor(() => expect(dispatch).toHaveBeenCalled());

    expect(createUserAction).toHaveBeenCalledWith({
      email: 'uliana@gmail.com',
      firstName: 'Uliana',
      lastName: 'Maksovna',
      password: 'asQW12',
      repeatPassword: 'asQW12',
    });
  });

  test('reset form', () => {
    const cleanUserStatus = jest.spyOn(userActions, 'cleanUserStatus');

    render(<RegisterForm />);

    const resetButton = screen.getByRole('button', {name: 'Reset'});
    const loginInput = screen.getByLabelText('Login Name');

    userEvent.type(loginInput, 'uliana@gmail.com');
    expect(loginInput).toHaveValue('uliana@gmail.com');

    userEvent.click(resetButton);

    expect(dispatch).toHaveBeenCalled();
    expect(cleanUserStatus).toHaveBeenCalled();
    expect(loginInput).toHaveValue('');
  });

  test('side effect when user created', () => {
    const navigate = jest.fn();
    mockedUseSelector.mockReturnValue('created');
    mockedUseNavigate.mockReturnValue(navigate);

    render(<RegisterForm />);

    expect(navigate).toHaveBeenCalledWith('/login');
  });
});
