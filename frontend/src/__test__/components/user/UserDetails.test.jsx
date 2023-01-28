import {render, screen} from '@testing-library/react';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import {createPaymentList} from './testData';
import UserDetails from '../../../components/user/UserDetails';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('user details', () => {
  const user = {id: 1, firstName: 'Vasiliy', lastName: 'Alibabaev'};
  const payments = createPaymentList(1, 1);

  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseSearchParams = jest.spyOn(routerHooks, 'useSearchParams');
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSearchParams.mockReturnValue([{get: jest.fn()}]);
    mockedUseSelector.mockReturnValueOnce(user).mockReturnValue({
      status: 'idle',
      payments,
      error: '',
      page: {totalPages: 5},
    });
  });

  test('user payment list render', () => {
    render(<UserDetails user={user} />);

    expect(screen.getByText('Vasiliy Alibabaev')).toBeInTheDocument();
  });
});
