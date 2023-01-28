import {screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom';
import Logo from '../../../components/header/Logo';
import {renderWithMui} from '../../helpers/renderWithMui';

describe('header logo component', () => {
  test('header logo render', () => {
    renderWithMui(
      <MemoryRouter>
        <Logo />
      </MemoryRouter>
    );

    expect(screen.getByText('Certifi')).toBeInTheDocument();
    expect(screen.getByAltText('cat')).toHaveAttribute(
      'src',
      '/assets/cat-face.svg'
    );
    expect(screen.getByText('e')).toBeInTheDocument();
  });
});
