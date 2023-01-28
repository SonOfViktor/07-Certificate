import {render, screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom';
import SignUpLink from '../../../components/login/SignUpLink';

describe('sign up link component', () => {
  test('sign up link component render', () => {
    render(
      <MemoryRouter>
        <SignUpLink />
      </MemoryRouter>
    );

    const createLink = screen.getByText('Create it.');
    expect(createLink).toHaveAttribute('href', '/register');
  });
});
