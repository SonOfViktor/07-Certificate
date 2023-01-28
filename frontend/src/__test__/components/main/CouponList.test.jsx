import {render, screen} from '@testing-library/react';
import * as reduxHooks from 'react-redux';
import CouponList from '../../../components/main/CouponList';
import {createCertificateList} from '../../helpers/certificateHelper';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('coupon list component', () => {
  const certificateList = createCertificateList(5);
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    window.IntersectionObserver = jest.fn().mockReturnValue({
      observe: () => null,
      unobserve: () => null,
      disconnect: () => null,
    });
  });

  test('coupon list component render with last element', () => {
    mockedUseSelector.mockReturnValue({
      certificateList,
      status: 'idle',
      error: '',
    });

    render(<CouponList />);

    expect(screen.getAllByAltText(/certificate/i).length).toBe(5);
    expect(screen.getByTestId('last-element')).toBeInTheDocument();
  });

  test('coupon list component render when status loading', () => {
    mockedUseSelector.mockReturnValue({
      certificateList,
      status: 'loading',
      error: '',
    });

    render(<CouponList />);

    expect(screen.getAllByAltText(/certificate/i).length).toBe(5);
    expect(screen.queryByTestId('last-element')).toBeNull();
  });
});
