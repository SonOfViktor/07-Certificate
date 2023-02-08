import {render, screen} from '@testing-library/react';
import BigLogo from '../../../components/login/BigLogo';

describe('certificate logo on login page', () => {
  test('certificate logo render', () => {
    render(<BigLogo />);

    expect(screen.getByText('CER')).toBeInTheDocument();
    expect(screen.getByText('TIF')).toBeInTheDocument();
    expect(screen.getByText('IE')).toBeInTheDocument();
    expect(screen.getByAltText('cat')).toHaveAttribute(
      'src',
      'assets/cat-face.svg'
    );
  });
});
