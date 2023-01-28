import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import CategorySelect from '../../../components/header/CategorySelect';
import {useQueryParams} from '../../../components/header/hooks/useQueryParams';

jest.mock('react-redux');
jest.mock('react-router-dom');
jest.mock('../../../components/header/hooks/useQueryParams');

describe('category selector', () => {
  const changeQueryParams = jest.fn();
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    useQueryParams.mockReturnValue([changeQueryParams]);
    mockedUseSelector.mockReturnValue(['Food', 'Sport', 'Tech']);
  });

  test('category selector render with default value', () => {
    mockedUseSelector.mockReturnValueOnce('');

    render(<CategorySelect />);

    expect(screen.getByText('All Categories')).toBeInTheDocument();
  });

  test('select category render', () => {
    mockedUseSelector.mockReturnValueOnce('Sport');

    render(<CategorySelect />);

    expect(screen.getByText('Sport')).toBeInTheDocument();
  });

  test('select another category', () => {
    mockedUseSelector.mockReturnValueOnce('');

    render(<CategorySelect />);

    const categorySelector = screen.getByRole('button', {name: 'categories'});

    userEvent.click(categorySelector);
    userEvent.click(screen.getByRole('option', {name: 'Food'}));

    expect(changeQueryParams).toHaveBeenCalledWith('category', 'Food');
  });
});
