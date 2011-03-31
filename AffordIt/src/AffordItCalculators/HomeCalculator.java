/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AffordItCalculators;



/**
 *
 * @author Tom Shulruff
 * 
 */
public class HomeCalculator 
{
    public class RangeException extends java.lang.Exception 
    {
        public RangeException(java.lang.String reason)
        {
            super(reason);
        }
    }
    
    // Constructor (run the default calculation)
    public HomeCalculator()
    {
        Update();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Inputs">
    //
    // Inputs into the algorithm.
    //
    
    // Debt Ratio
    private double _rho = 0.35;
    public double getDebtRatio() { return _rho; }
    public void setDebtRatio(double DebtRatio) throws RangeException
    {
        if (0.0 >= DebtRatio) 
            throw new RangeException("Error: Negative debt ratio.");
        if (1.0 <= DebtRatio)
            throw new RangeException("Error: Can't spend more money than you "
                    + "make.");
        
        this._rho = DebtRatio;
    }
    public double getDebtRatioPercent() { return _rho * 100.0; }
    public void setDebtRatioPercent(double DebtRatio) throws RangeException
    {
        if (0.0 >= DebtRatio) 
            throw new RangeException("Error: Negative debt ratio.");
        if (100.0 <= DebtRatio)
            throw new RangeException("Error: Can't spend more money than you "
                    + "make.");
        this._rho = DebtRatio / 100.0;
    }

    // $ Income /yr.
    private double _I = 100000.00;
    public double getIncome() { return _I; }
    public void setIncome(double Income) throws RangeException 
    {
        if (0.0 >= Income) 
            throw new RangeException("Error: Positive income is required to "
                    + "buy a house.");
        this._I = Income;
    }
    public double getMonthlyIncome() { return _I / 12.0; }
    public void setMonthlyIncome(double Income) throws RangeException
    {
        if (0.0 >= Income) 
            throw new RangeException("Error: Positive income is required to "
                    + "buy a house.");
        this._I = Income * 12.0;
    }

    // $ Insurance /yr.
    private double _s = 1000.00;
    public double getInsurance() { return _s; }
    public void setInsurance(double Insurance) throws RangeException
    {
        if (0.0 >= Insurance) 
            throw new RangeException("Error: Insurance is required to buy a "
                    + "house.");
        this._s = Insurance;
    }
    public double getMonthlyInsurance() { return _s / 12.0; }
    public void setMonthlyInsurance(double Insurance) throws RangeException
    {
        if (0.0 >= Insurance) 
            throw new RangeException("Error: Insurance is required to buy a "
                    + "house.");
        this._s = Insurance * 12.0;
    }

    // $ Additional payments /yr.
    private double _delta = 0.00;
    public double getAdditionalDebt() { return _delta; }
    public void setAdditionalDebt(double AdditionalDebt) throws RangeException
    {
        if (0.0 > AdditionalDebt) 
            throw new RangeException("Error: Add additional income to the "
                    + "Income field.");
        this._delta = AdditionalDebt;
    }
    public double getMonthlyAdditionalDebt() { return _delta / 12.0; }
    public void setMonthlyAdditionalDebt(double AdditionalDebt)
            throws RangeException
    {
        if (0.0 > AdditionalDebt) 
            throw new RangeException("Error: Add additional income to the "
                    + "Income field.");
        this._delta = AdditionalDebt * 12.0;
    }

    // $ Down Payment
    private double _D = 150000.00;
    public double getDownPayment() { return _D; }
    public void setDownPayment(double DownPayment) throws RangeException
    {
        if (0.0 > DownPayment) 
            throw new RangeException("Error: A significant down payment is "
                    + "required to purchase a home.");
        this._D = DownPayment;
    }

    // $ Yearly HOA Dues.
    private double _HOA = 0.00;
    public double getHOADues() { return _HOA; }
    public void setHOADues(double HOADues) throws RangeException {
        if (0.0 > HOADues) 
            throw new RangeException("Error: No HOA will pay you to live at "
                    + "a place.");
        this._HOA = HOADues;
    }
    public double getMonthlyHOADues() { return _HOA / 12.0; }
    public void setMonthlyHOADues(double HOADues) throws RangeException {
        if (0.0 > HOADues) 
            throw new RangeException("Error: No HOA will pay you to live at "
                    + "a place.");
        this._HOA = HOADues * 12.0;
    }

    // Property Tax Rate
    private double _t = 0.013;
    public double getTaxRate() { return _t; }
    public void setTaxRate(double TaxRate) throws RangeException 
    {
        if (0.0 >= TaxRate)
            throw new RangeException("Error: Invalid Property Tax.");
        this._t = TaxRate;
    }
    public double getTaxRatePercent() { return _t * 100.0; }
    public void setTaxRatePercent(double TaxRate) throws RangeException 
    {
        if (0.0 >= TaxRate)
            throw new RangeException("Error: Invalid Property Tax.");
        this._t = TaxRate / 100.0;
    }

    // APR (Fixed Rate)
    private double _i = 0.0625;
    public double getAPR() { return _i; }
    public void setAPR(double APR) throws RangeException
    {
        if (0.0 >= APR)
            throw new RangeException("Error: Invalid annual percentage rate.");
        this._i = APR;
    }
    public double getAPRPercent() { return _i * 100.0; }
    public void setAPRPercent(double APR) throws RangeException
    {
        if (0.0 >= APR)
            throw new RangeException("Error: Invalid annual percentage rate.");
        this._i = APR / 100.0;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Outputs">                          
    //
    // Computed results and temporary numbers.
    //

    // $ Max Home Price
    private double _H = 0.0;
    public double getMaxHomePrice() { return _H; }

    // $ Annual income available for payments on principal and interest.
    private double _A = 0.0;
    // TODO - Consider computing monthly leftover for budgeting...

    // Scale factor to get principal from available annual income.
    private double _X = 0.0;
    public double getMoxie() { return _X; }
    
    // String table for Recommentdations.
    private String[] _Recommendations = 
    {
        "",
        "Debt ratio is the limiting factor. Earn more money.",
        "Maximize your down payment.",
        "Earn more money.",
        "Minimize your insurance premium, HOA, or other payments.",
        "Minimize the interest rate on your mortgage."
    };

    // Particular recommendation based on the numbers.
    /**
     * Recommendation by the Index:
     * 0 = invalid
     * 1 = Debt Ratio.
     * 2 = Down Payment.
     * 3 = Income.
     * 4 = Insurance/HOA/Other Debt
     * 5 = Interest.
     */
    private int _iRecommendation = 0;
    public String Recommendation() {
        return _Recommendations[_iRecommendation]; }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Utilities">                          
    //
    // Compute the outputs and make recommendation.
    //
    
    // Assume the inputs are correct and perform the calculations.
    public final void Update()
    {
        _ComputeMoxie();
        _ComputeAvailableIncome();
        _ComputeMaxHomePrice();
        _DetermineMaxInfluence();
    }
    
    
    private void _ComputeMoxie()
    {
        _X = _Moxie(_i);
    }
    
    private double _Moxie(double i)
    {
        double x = Math.pow((i/12.0)+ 1, 360.0);
        return 1.0 / (_t + (i * (x / (x - 1.0))));
    }
    
    private void _ComputeAvailableIncome()
    {
        _A = (_rho * _I) - (_t * _D) - ((_s + _HOA + _delta) / 0.6);
    }
    
    private void _ComputeMaxHomePrice()
    {
        _H = (_X * _A) + _D;
    }
    
    private void _DetermineMaxInfluence()
    {
        // Start with Insurance/HOA/Other Debt.
        _iRecommendation = 4;
        double MaxInfluence = Math.abs(_X);

        // Consider debt ratio.
        double Influence = Math.abs(_I * _X);
        if (Influence > MaxInfluence)
        {
            _iRecommendation = 1;
            MaxInfluence = Influence;
        }
        
        // Consider Income
        Influence = Math.abs(_rho * _X);
        if (Influence > MaxInfluence)
        {
            _iRecommendation = 3;
            MaxInfluence = Influence;
        }
        
        // Consider Down Payment
        Influence = Math.abs(1.0 - (_t * _X));
        if (Influence > MaxInfluence)
        {
            _iRecommendation = 2;
            MaxInfluence = Influence;
        }
        
        // Consider APR
        Influence = Math.abs(_InfluenceFromAPR());
        if (Influence > MaxInfluence)
        {
            _iRecommendation = 5;
            // no more checks...
        }
    }
    
    // Do a numerical derivative (secant method) since closed form derivative is
    // a beast.
    private double _InfluenceFromAPR()
    {
        double dMoxie_di = (_Moxie(_i + 0.0001) - _Moxie(_i - 0.0001)) / 0.0002;
        return _A * dMoxie_di;
    }
    
    // </editor-fold>
    
}
