import {render, screen} from '@testing-library/react';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import CategoryList from '../../../components/main/CategoryList';
import {createMatchMedia} from '../../helpers/createMatchMedia';
import {act} from 'react-dom/test-utils';
import {categoryList} from './testData';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('category list on main page', () => {
  const setSearchParams = jest.fn();
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseSearchParams = jest.spyOn(routerHooks, 'useSearchParams');

  beforeAll(() => {
    jest.useFakeTimers();
  });

  beforeEach(() => {
    mockedUseSelector.mockReturnValue(categoryList);
    mockedUseSearchParams.mockReturnValue([null, setSearchParams]);
  });

  test('render category list', () => {
    window.innerWidth = 1100;
    window.matchMedia = createMatchMedia(window.innerWidth);

    render(<CategoryList />);

    const buttons = screen.getAllByRole('button');
    expect(buttons.length).toBe(6);
  });

  test('render category list with window width less 1100', () => {
    window.innerWidth = 1099;
    window.matchMedia = createMatchMedia(window.innerWidth);

    render(<CategoryList />);

    const buttons = screen.getAllByRole('button');
    expect(buttons.length).toBe(4);
  });

  test('click on category', () => {
    render(<CategoryList />);

    const buttons = screen.getAllByRole('button');

    expect(setSearchParams).not.toHaveBeenCalled();
    userEvent.click(buttons[0]);
    act(() => {
      jest.runAllTimers();
    });
    expect(setSearchParams).toHaveBeenCalled();
  });

  afterAll(() => {
    jest.useRealTimers();
  });
});
