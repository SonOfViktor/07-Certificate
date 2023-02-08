import {render} from '@testing-library/react';
import GlobalThemeProvider from '../../components/provider/GlobalThemeProvider';

export const renderWithMui = component => {
  return render(<GlobalThemeProvider>{component}</GlobalThemeProvider>);
};
