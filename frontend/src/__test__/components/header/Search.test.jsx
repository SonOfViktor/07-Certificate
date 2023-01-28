import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import {useQueryParams} from '../../../components/header/hooks/useQueryParams';
import Search from '../../../components/header/Search';

jest.mock('../../../components/header/hooks/useQueryParams');
jest.mock('react-redux');
jest.mock('react-router-dom');

describe('user payment list', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseLocation = jest.spyOn(routerHooks, 'useLocation');
  const dispatch = jest.fn();
  const changeQueryParams = jest.fn();
  const initStateValue = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSelector
      .mockReturnValueOnce('')
      .mockReturnValue(['Food', 'Sport', 'Tech']);
    initStateValue.mockReturnValue('');
    useQueryParams.mockReturnValue([changeQueryParams, initStateValue]);
    mockedUseLocation.mockReturnValue({pathname: '/'});
  });

  test('search component render', () => {
    render(<Search />);

    expect(screen.getByPlaceholderText('Search by name')).toHaveValue('');
  });

  test('type in search input', () => {
    render(<Search />);

    const searchInput = screen.getByPlaceholderText('Search by name');

    userEvent.type(searchInput, 'search');
    expect(searchInput).toHaveValue('search');
    expect(changeQueryParams).toHaveBeenNthCalledWith(
      6,
      'search',
      'search',
      1000
    );
  });
});
