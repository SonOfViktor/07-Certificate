import {render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as routerHooks from 'react-router-dom';
import AdminLink from '../../../../components/header/menu/AdminLink';

jest.mock('../../../../components/header/hooks/useQueryParams');
jest.mock('react-redux');
jest.mock('react-router-dom');

describe('filter component', () => {
  const mockedUseNavigate = jest.spyOn(routerHooks, 'useNavigate');
  const navigate = jest.fn();

  beforeEach(() => {
    mockedUseNavigate.mockReturnValue(navigate);
  });

  test('click admin link', () => {
    const onClose = jest.fn();

    render(<AdminLink onClose={onClose} />);

    userEvent.click(screen.getByText('Admin'));

    expect(navigate).toHaveBeenCalledWith('/admin/certificate');
    expect(onClose).toHaveBeenCalled();
  });
});
