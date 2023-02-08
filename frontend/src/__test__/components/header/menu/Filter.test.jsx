import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import Filter from '../../../../components/header/menu/Filter';
import {useQueryParams} from '../../../../components/header/hooks/useQueryParams';

jest.mock('../../../../components/header/hooks/useQueryParams');
jest.mock('react-redux');
jest.mock('react-router-dom');

describe('filter component', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseLocation = jest.spyOn(routerHooks, 'useLocation');
  const dispatch = jest.fn();
  const changeQueryParams = jest.fn();
  const initStateValue = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSelector.mockReturnValue([]);
    initStateValue.mockReturnValue('');
    useQueryParams.mockReturnValue([changeQueryParams, initStateValue]);
    mockedUseLocation.mockReturnValue({pathname: '/'});
  });

  test('filter component render with default values', () => {
    render(<Filter />);

    expect(screen.getByLabelText('Description')).toBeInTheDocument();
    expect(screen.getByLabelText('Tags')).toBeInTheDocument();
  });

  test('filter component render with query parameters', () => {
    initStateValue.mockReturnValue('text');
    mockedUseSelector.mockReturnValue(['tag1', 'tag2']);

    render(<Filter />);

    expect(screen.getByLabelText('Description')).toHaveValue('text');
    expect(screen.getAllByText(/tag\d/)).toHaveLength(2);
  });

  test('type in description input', () => {
    render(<Filter />);

    const descriptionInput = screen.getByLabelText('Description');

    userEvent.type(descriptionInput, 'text');
    expect(descriptionInput).toHaveValue('text');
    expect(changeQueryParams).toHaveBeenNthCalledWith(
      4,
      'description',
      'text',
      1000
    );
  });

  test('add tag in filter', () => {
    render(<Filter />);

    const tagInput = screen.getByLabelText('Tags');
    const addButton = screen.getByRole('button', {name: 'Add'});

    userEvent.type(tagInput, 'tag');
    expect(tagInput).toHaveValue('tag');

    userEvent.click(addButton);
    expect(changeQueryParams).toHaveBeenCalledWith('tags', 'tag');
    expect(tagInput).toHaveValue('');
  });

  test('add the same tag', () => {
    mockedUseSelector.mockReturnValue(['tag1', 'tag2']);

    render(<Filter />);

    const tagInput = screen.getByLabelText('Tags');
    const addButton = screen.getByRole('button', {name: 'Add'});

    userEvent.type(tagInput, 'tag1');
    userEvent.click(addButton);
    expect(changeQueryParams).not.toHaveBeenCalled();
    expect(tagInput).toHaveValue('');
  });

  test('delete tag', () => {
    mockedUseSelector.mockReturnValue(['tag1', 'tag2', 'tag3']);

    render(<Filter />);

    const deleteTagButtons = screen.getAllByTestId('CancelIcon');

    userEvent.click(deleteTagButtons[0]);
    expect(changeQueryParams).toHaveBeenCalledWith('tags', 'tag2,tag3');
  });
});
