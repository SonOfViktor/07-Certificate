import {render, screen} from '@testing-library/react';
import UserAvatar from '../../../components/header/UserAvatar';

describe('user avatar', () => {
  test('user avatar render', () => {
    render(<UserAvatar firstName="Maks" lastName="Sil" />);

    expect(screen.getByText('MS')).toBeInTheDocument();
  });
});
