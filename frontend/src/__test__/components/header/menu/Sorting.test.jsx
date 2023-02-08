import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import Sorting from '../../../../components/header/menu/Sorting';
import {useQueryParams} from '../../../../components/header/hooks/useQueryParams';

jest.mock('../../../../components/header/hooks/useQueryParams');
jest.mock('react-redux');
jest.mock('react-router-dom');

describe('user payment list', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const dispatch = jest.fn();
  const changeQueryParams = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSelector.mockReturnValue({field: 'createDate', order: 'ASC'});
    useQueryParams.mockReturnValue([changeQueryParams]);
  });

  test('sorting component render', () => {
    render(<Sorting />);

    expect(screen.getByText('Create Date')).toBeInTheDocument();
    expect(screen.getByTestId('KeyboardArrowUpIcon')).toBeInTheDocument();
  });

  test('change sorting field', () => {
    render(<Sorting />);

    const changeFieldButton = screen.getByRole('button', {name: 'sort'});
    userEvent.click(changeFieldButton);
    expect(screen.getAllByRole('option')).toHaveLength(3);

    userEvent.click(screen.getByText('Name'));

    expect(changeQueryParams).toHaveBeenCalledWith('sort', 'name,ASC');
  });

  test('change sorting order', () => {
    render(<Sorting />);

    userEvent.click(screen.getByRole('checkbox'));
    expect(changeQueryParams).toHaveBeenCalledWith('sort', 'createDate,DESC');
  });
});
